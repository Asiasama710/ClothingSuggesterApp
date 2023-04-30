package com.asiasama.clothingsuggesterapp.data.local

import com.asiasama.clothingsuggesterapp.data.remote.responce.Clothing

class ClothesDatasource {
    fun getClothes(temperature: Int): List<Clothing> {
        return when (temperature) {
            in 1..10 -> listOf(
                Clothing("1", "Jacket", "top", "0-10", "https://i.pinimg.com/236x/9c/be/f8/9cbef8f6729dd2ced09873694b1b0988.jpg"),
                Clothing("2", "T-shirt", "top", "0-10", "https://i.pinimg.com/236x/c7/36/07/c736079c2ad6f8ceb14b4eb1f296f24a.jpg"),
                Clothing("3", "Jeans", "bottom", "0-10", "https://i.pinimg.com/564x/7c/09/8b/7c098b3dd468cabb04215b175206242f.jpg"),
                Clothing("4", "Shoes", "shoes", "0-10", "https://i.pinimg.com/236x/57/cd/c0/57cdc064cd5f53716577eba7f7e7a624.jpg")
            )
            in 11..20 -> listOf(
                Clothing("5", "T-shirt", "top", "11-20", "https://i.pinimg.com/564x/e6/5d/ef/e65def293c54cf8199a16d39238e90bc.jpg"),
                Clothing("6", "Jeans", "bottom", "11-20", "https://i.pinimg.com/564x/53/56/d3/5356d354c34ab36420a1bb1db0ca3f64.jpg"),
                Clothing("7", "Shoes", "shoes", "11-20", "https://i.pinimg.com/564x/ad/db/d2/addbd218dac62d90ae974b6a6614aa20.jpg"),
                Clothing("8", "Shoes", "shoes", "11-20", "https://i.pinimg.com/564x/bf/40/d2/bf40d2f6484e3f31e42b9036abc78d81.jpg")

            )
            in 21..30 -> listOf(
                Clothing("9", "T-shirt", "top", "21-30", "https://i.pinimg.com/236x/1b/b7/6e/1bb76eb3ce7451a5c315604732f223e9.jpg"),
                Clothing("10", "Shorts", "bottom", "21-30", "https://i.pinimg.com/564x/69/ad/ae/69adae2e1a485464b105963b782db9d7.jpg"),
                Clothing("11", "Shoes", "shoes", "21-30", "https://i.pinimg.com/564x/b4/2c/db/b42cdbaa38229a9eea847cad58678648.jpg"),
                Clothing("12", "Shoes", "shoes", "21-30", "https://i.pinimg.com/564x/94/9d/71/949d71eb94de4c7e1c2f427633775f59.jpg")

            )
            else -> listOf(
                Clothing("13", "T-shirt", "top", "31-40", "https://i.pinimg.com/564x/bb/30/cc/bb30cc36ac06c10eb5b018c2373539a8.jpg"),
                Clothing("14", "Shorts", "bottom", "31-40", "https://i.pinimg.com/564x/5a/d3/85/5ad3858c5d79022482be451bff2137b6.jpg"),
                Clothing("15", "Shoes", "shoes", "31-40", "https://i.pinimg.com/564x/46/a1/46/46a146626516a2aa58d2a3f78a64dcb9.jpg"),
                Clothing("16", "Shoes", "shoes", "31-40", "https://i.pinimg.com/564x/2d/be/82/2dbe82704d40931a03aa3c2e4e0e3893.jpg")

            )

        }
    }
}