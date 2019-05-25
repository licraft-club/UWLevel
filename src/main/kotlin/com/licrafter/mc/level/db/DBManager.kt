package com.licrafter.mc.level.db

import com.licrafter.lib.db.DataBaseType
import com.licrafter.mc.level.LevelPlugin

class DBManager {

    private var repository: Repository? = null
    private var dbType: DataBaseType = DataBaseType.Sqlite

    fun startDatabase() {
        val type = LevelPlugin.levelConfig().storage?.type
        type?.let {
            if (it.equals("mysql", false)) {
                dbType = DataBaseType.MySql
                repository = MysqlRepository()
            } else {
                dbType = DataBaseType.Sqlite
                repository = SqliteRepository()
            }
            repository?.init()
        }
    }

    fun getRepository(): Repository? {
        return repository
    }
}