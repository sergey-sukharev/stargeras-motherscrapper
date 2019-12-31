package scrappers.vk.data.repository

import java.lang.Exception

class RegionNotFoundException: Exception {

    constructor() : super()
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}