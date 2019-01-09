package com.wxsoft.fcare.ui

interface EventActions:EventAction<String>

interface  EventAction<T>{
    fun onOpen(t:T)
}