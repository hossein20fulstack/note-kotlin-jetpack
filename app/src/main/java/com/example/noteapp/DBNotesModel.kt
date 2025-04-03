package com.example.noteapp

data class DBNotesModel(
    var id: Int,
    var title: String,
    var detail: String,
    var delete_state: String,
    var date: String
)
