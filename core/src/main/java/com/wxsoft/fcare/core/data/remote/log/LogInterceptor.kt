package com.wxsoft.fcare.core.data.remote.log


import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

class LogInterceptor @JvmOverloads constructor(private val logger: Logger = Logger.DEFAULT) : Interceptor {

    @Volatile
    private var level = Level.NONE

    enum class Level {
        NONE,
        HEADERS,
        BODY
    }

    interface Logger {
        fun log(message: String)

        companion object {

            val DEFAULT: Logger = object : Logger {
                override fun log(message: String) {
                    //Platform.get().log(4, message, null);
                }
            }
        }
    }

    /**
     * Change the level at which this interceptor logs.
     */
    fun setLevel(level: Level?): LogInterceptor {
        if (level == null) throw NullPointerException("level == null. Use Level.NONE instead.")
        this.level = level
        return this
    }

    fun getLevel(): Level {
        return level
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        var requestStartMessage = "--> " + request.method() + ' '.toString() + request.url() + ' '.toString() + protocol(protocol)
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        logger.log(requestStartMessage)

        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody!!.contentType() != null) {
                    logger.log("Content-Type: " + requestBody.contentType()!!)
                }
                if (requestBody.contentLength() != -1L) {
                    logger.log("Content-Length: " + requestBody.contentLength())
                }
            }

            val headers = request.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
                    logger.log(name + ": " + headers.value(i))
                }
                i++
            }

            if (!logBody || !hasRequestBody) {
                logger.log("--> END " + request.method())
            } else if (bodyEncoded(request.headers())) {
                logger.log("--> END " + request.method() + " (encoded body omitted)")
            } else {
                val buffer = Buffer()
                requestBody!!.writeTo(buffer)

                var charset: Charset? = UTF8
                val contentType = requestBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                logger.log("")
                logger.log(buffer.readString(charset!!))

                logger.log(
                        "--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)")
            }
        }

        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength != -1L) contentLength.toString() + "-byte" else "unknown-length"
        logger.log("<-- code: "
                + response.code()+ ' '.toString()+ response.message()+ ' '.toString()+ response.request().url() + " ("
                + tookMs + "ms" + (if (!logHeaders) ", $bodySize body" else "") + ')'.toString())

        if (logHeaders) {
            val headers = response.headers()
            var i = 0
            val count = headers.size()
            while (i < count) {
                logger.log(headers.name(i) + ": " + headers.value(i))
                i++
            }

            if (!logBody || !hasRequestBody) {
                logger.log("<-- END HTTP")
            } else if (bodyEncoded(response.headers())) {
                logger.log("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()

                var charset: Charset? = UTF8
                val contentType = responseBody.contentType()
                if (contentType != null) {
                    charset = contentType.charset(UTF8)
                }

                if (contentLength != 0L) {
                    logger.log("")
                    logger.log(buffer.clone().readString(charset!!))
                }

                logger.log("<-- END HTTP (" + buffer.size() + "-byte body)")
            }
        }

        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    companion object {

        private val UTF8 = Charset.forName("UTF-8")

        private fun protocol(protocol: Protocol): String {
            return when(protocol){
                Protocol.HTTP_1_0->{"HTTP/1.0"}
                Protocol.HTTP_1_1->{"HTTP/1.1"}
                Protocol.HTTP_2->{"HTTP/2"}
                Protocol.H2_PRIOR_KNOWLEDGE->{"HTTP-2-PRIOR-KNOWLEDGE"}
                Protocol.QUIC->{"QUIC"}
                else->"OTHER PROTOCOL"
            }
        }
    }
}
