package com.example.myurltester.models

class UrlItem {
    var id: Long = 0
    var path: String? = null
    var isAccessible: Boolean = false
    var isChecked: Boolean = false
    var responseTime: Long = 0

    constructor()

    constructor(id: Long, path: String?) {
        this.id = id
        this.path = path
    }
}