package com.example.tubes_kelompok10.entity

class Home(var name:String, var position:String) {
    companion object{
        @JvmField
        var listofHome= arrayOf(
            Home("TokoPedia", "Digital Marketing"),
            Home("BCA", "Teller"),
            Home("BNI", "Customer Service"),
            Home("Shopee", "Customer Service"),
            Home("Lazada", "Web Developer"),
            Home("PT KAI", "Keuangan"),
            Home("PT Kimia Farma", "Assessor"),
            Home("Perum Bulog", "Staff Pelaksana"),
            Home("PT Telkom Indonesia", "Customer Engagement"),
            Home("BRI", "Data Analyst")
        )
    }
}