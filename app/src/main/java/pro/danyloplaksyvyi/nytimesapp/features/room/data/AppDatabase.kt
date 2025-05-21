package pro.danyloplaksyvyi.nytimesapp.features.room.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.BookDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.ListDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.ListMetadataDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.dao.OverviewMetadataDao
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.BookEntity
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.ListEntity
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.ListMetadata
import pro.danyloplaksyvyi.nytimesapp.features.room.data.entity.OverviewMetadata

@Database(
    entities = [
        ListEntity::class,
        BookEntity::class,
        OverviewMetadata::class,
        ListMetadata::class
    ],
    version = 3, // Increment to version 3
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun listDao(): ListDao
    abstract fun bookDao(): BookDao
    abstract fun overviewMetadataDao(): OverviewMetadataDao
    abstract fun listMetadataDao(): ListMetadataDao
}

// Migrations
val MIGRATION_1_2 = Migration(1, 2) { database ->
    // Create a temporary table with the correct schema
    database.execSQL("""
        CREATE TABLE books_temp (
            bookId TEXT PRIMARY KEY NOT NULL,
            listId INTEGER NOT NULL,
            listNameEncoded TEXT NOT NULL DEFAULT '', -- Non-nullable with default
            rank INTEGER NOT NULL,
            title TEXT NOT NULL,
            author TEXT NOT NULL,
            publisher TEXT NOT NULL,
            description TEXT,
            bookImage TEXT NOT NULL,
            amazonProductUrl TEXT NOT NULL,
            FOREIGN KEY (listId) REFERENCES lists(listId) ON DELETE CASCADE
        )
    """)
    // Copy data from old table to new table, providing default for listNameEncoded
    database.execSQL("""
        INSERT INTO books_temp (
            bookId, listId, listNameEncoded, rank, title, author, publisher,
            description, bookImage, amazonProductUrl
        )
        SELECT bookId, listId, COALESCE(listNameEncoded, '') AS listNameEncoded,
               rank, title, author, publisher, description, bookImage, amazonProductUrl
        FROM books
    """)
    // Drop old table and rename temporary table
    database.execSQL("DROP TABLE books")
    database.execSQL("ALTER TABLE books_temp RENAME TO books")
    // Create index
    database.execSQL("CREATE INDEX index_books_listId ON books(listId)")
}

val MIGRATION_2_3 = Migration(2, 3) { database ->
    // Check if list_metadata exists to avoid "table already exists" error
    database.execSQL("""
        CREATE TABLE IF NOT EXISTS list_metadata (
            listName TEXT PRIMARY KEY NOT NULL,
            publishedDate TEXT NOT NULL,
            timestamp INTEGER NOT NULL
        )
    """)
}