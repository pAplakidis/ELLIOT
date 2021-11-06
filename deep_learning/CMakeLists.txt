cmake_minimum_required(VERSION 3.2 FATAL_ERROR)
project(ELLIOT)

set(CMAKE_PREFIX_PATH /home/paul/libtorch)
set(CMAKE_BUILD_TYPE Debug)
find_package(Torch REQUIRED)
find_package(OpenCV REQUIRED)
find_package(jsoncpp REQUIRED)


set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${TORCH_CXX_FLAGS}")

add_executable(${PROJECT_NAME} model.cc)
target_link_libraries(${PROJECT_NAME} "${TORCH_LIBRARIES}" "${OpenCV_LIBS}" jsoncpp)
set_property(TARGET ${PROJECT_NAME} PROPERTY CXX_STANDARD 14)

