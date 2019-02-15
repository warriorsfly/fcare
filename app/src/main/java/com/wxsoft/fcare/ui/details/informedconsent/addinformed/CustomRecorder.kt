package com.wxsoft.fcare.ui.details.informedconsent.addinformed

import omrecorder.AbstractRecorder
import omrecorder.PullTransport
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile

class CustomRecorder(pullTransport: PullTransport, var recordFile: File, val tempFile: File) : AbstractRecorder(pullTransport, recordFile) {
    override fun pauseRecording() {
        super.pauseRecording()
        try {
            if (tempFile.exists()) {
                tempFile.delete()
            }
            //            Files.copy(file.toPath(),tempFile.toPath());
            FileUtil.copyFile(recordFile.path, tempFile.path)
            writeWavHeader(tempFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun stopRecording() {
        try {
            super.stopRecording()
            writeWavHeader(recordFile)
        } catch (e: IOException) {
            throw RuntimeException("Error in applying wav header", e)
        }
    }

    @Throws(IOException::class)
    private fun writeWavHeader(targetFile: File) {
        val wavFile = randomAccessFile(targetFile)
        wavFile.seek(0) // to the beginning
        wavFile.write(WavHeader(pullTransport.pullableSource(), targetFile.length()).toBytes())
        wavFile.close()
    }

    private fun randomAccessFile(file: File): RandomAccessFile {
        val randomAccessFile: RandomAccessFile
        try {
            randomAccessFile = RandomAccessFile(file, "rw")
        } catch (e: FileNotFoundException) {
            throw RuntimeException(e)
        }
        return randomAccessFile
    }
}
