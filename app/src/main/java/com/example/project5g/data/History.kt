package com.example.project5g.data

import org.w3c.dom.Text
import java.sql.Date

data class History(
    var data : List<Purchases>?,
    var pagination: Pagination
)
data class Pagination(
    var totalCount: Int?,
    var pageSize: Int?,
    var currentPage: Int?,
    var totalPages: Int?,
    var hasNext: Boolean?,
    var hasPrevious: Boolean?,
)
