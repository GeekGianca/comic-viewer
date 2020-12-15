package com.gksoftwaresolutions.comicviewer.model

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    ENDOFLIST,
    EMPTY
}

class NetworkState(val status: Status, val msg: String) {
    companion object {
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")
        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")
        val ERROR: NetworkState = NetworkState(Status.FAILED, "Failed to load results")
        val ENDOFLIST: NetworkState = NetworkState(Status.ENDOFLIST, "No more results")
        val EMPTY: NetworkState =
            NetworkState(Status.EMPTY, "No find any match")
    }
}