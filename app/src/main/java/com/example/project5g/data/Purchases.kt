package com.example.project5g.data

import org.w3c.dom.Text
import java.sql.Date

data class Purchases(
    var id:String?,
    var customerId :String?,
    var userId:String?,
    var date:String?,
    var total: Double?,
    var saleDescription:List<SaleDescription>?,
    var customer: String?,
    var user:String?
)

//"id": "02e9fa55-46f1-436b-bbd9-22d02d666c7a",
//"customerId": "e30bab80-9eca-4205-bb0c-bfa971b8dfab",
//"userId": null,
//"date": "2024-06-04T07:22:43.713662Z",
//"total": 369,
//"saleDescription": [
//{
//    "id": "da52467b-7497-4c20-a5f3-a1c85bde138e",
//    "saleId": "02e9fa55-46f1-436b-bbd9-22d02d666c7a",
//    "productId": "14d58f30-fc60-4e4e-ae55-4b304879b682",
//    "price": 123,
//    "qty": 3,
//    "amount": 369,
//    "product": null
//}
//],
//"customer": null,
//"user": null