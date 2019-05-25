package com.licrafter.mc.level.db

/**
 * Created by shell on 2019/5/25.
 * <p>
 * Gmail: shellljx@gmail.com
 */
interface Repository {
    fun init()

    fun save()

    fun disable()
}