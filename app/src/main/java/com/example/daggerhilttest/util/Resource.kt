package com.example.daggerhilttest.util

 class Resource<T>(
     var status: String,
     var data: T? = null,
     var errorMessage: String? = null
 )

