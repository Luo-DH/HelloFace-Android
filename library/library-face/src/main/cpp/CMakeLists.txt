cmake_minimum_required(VERSION 3.10.2)

project("face")

set(ncnn_DIR ${CMAKE_SOURCE_DIR}/ncnn-20201218-android-vulkan/${ANDROID_ABI}/lib/cmake/ncnn)
find_package(ncnn REQUIRED)

add_library(
        face
        SHARED
        #        MyTest.cpp
        InterfaceArcFace.cpp
        InterfaceRetinaFace.cpp
        arcface/ArcFace.cpp
        retinaface/RetinaFace.cpp
        native-lib.cpp
)


find_library(
        log-lib
        log
)

target_link_libraries(
        face
        ncnn
        ${log-lib}
        vulkan
        jnigraphics
)