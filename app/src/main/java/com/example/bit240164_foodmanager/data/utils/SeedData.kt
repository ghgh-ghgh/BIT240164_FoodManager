package com.example.bit240164_foodmanager.data.utils

import com.example.bit240164_foodmanager.data.model.Dish
import com.example.bit240164_foodmanager.data.model.DishCategory
import com.example.bit240164_foodmanager.data.model.DishImage

object SeedData {
    val categories = listOf(
        DishCategory(id = "1", BIT240164_Name = "Cơm & Phở", BIT240164_Description = "Các món cơm và phở truyền thống"),
        DishCategory(id = "2", BIT240164_Name = "Món nướng", BIT240164_Description = "Các món nướng thơm ngon đặc sắc"),
        DishCategory(id = "3", BIT240164_Name = "Đồ uống & Tráng miệng", BIT240164_Description = "Trà, sinh tố và đồ tráng miệng")
    )

    val dishes = listOf(
        Dish(
            id = "1",
            BIT240164_Name = "Phở Bò",
            BIT240164_Price = 45000.0,
            BIT240164_PreparationTime = 20,
            BIT240164_IsAvailable = true,
            BIT240164_Description = "Traditional Vietnamese beef noodle soup",
            BIT240164_DishCategoryId = "1",
            BIT240164_DishImage = listOf(
                DishImage("https://cdn2.fptshop.com.vn/unsafe/1920x0/filters:format(webp):quality(75)/cach_nau_pho_bo_nam_dinh_0_1d94be153c.png", true),
                DishImage("https://picsum.photos/400/300?random=12", false)
            )
        ),
        Dish(
            id = "2",
            BIT240164_Name = "Bún Chả",
            BIT240164_Price = 40000.0,
            BIT240164_PreparationTime = 15,
            BIT240164_IsAvailable = true,
            BIT240164_Description = "Grilled pork with noodles",
            BIT240164_DishCategoryId = "1",
            BIT240164_DishImage = listOf(
                DishImage("https://down-vn.img.susercontent.com/vn-11134259-7ras8-mdbn3lyetzu55e", true)
            )
        ),
        Dish(
            id = "3",
            BIT240164_Name = "Gỏi Cuốn",
            BIT240164_Price = 30000.0,
            BIT240164_PreparationTime = 10,
            BIT240164_IsAvailable = true,
            BIT240164_Description = "Fresh spring rolls",
            BIT240164_DishCategoryId = "2",
            BIT240164_DishImage = listOf(
                DishImage("https://tse1.mm.bing.net/th/id/OIP.qwM6LaWBaLNmVR0ibqVuVQHaLH?r=0&rs=1&pid=ImgDetMain&o=7&rm=3", true)
            )
        ),
        Dish(
            id = "4",
            BIT240164_Name = "Trà Đá",
            BIT240164_Price = 10000.0,
            BIT240164_PreparationTime = 5,
            BIT240164_IsAvailable = true,
            BIT240164_Description = "Iced tea",
            BIT240164_DishCategoryId = "3",
            BIT240164_DishImage = listOf(
                DishImage("https://tse3.mm.bing.net/th/id/OIP.pDW-KzlnJM6Wf3z8lfxvogAAAA?r=0&rs=1&pid=ImgDetMain&o=7&rm=3", true)
            )
        ),
        Dish(
            id = "5",
            BIT240164_Name = "Cà Phê Sữa",
            BIT240164_Price = 25000.0,
            BIT240164_PreparationTime = 8,
            BIT240164_IsAvailable = true,
            BIT240164_Description = "Vietnamese iced coffee with milk",
            BIT240164_DishCategoryId = "3",
            BIT240164_DishImage = listOf(
                DishImage("https://tse1.explicit.bing.net/th/id/OIP.WlWbzxRgeQy7eK9U9LHlCAHaFy?r=0&rs=1&pid=ImgDetMain&o=7&rm=3", true)
            )
        )
    )
}
