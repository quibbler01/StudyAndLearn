package cn.quibbler.coroutine.jitpack.recyclerview

 class MockData(val title: String, val content: String) {

    companion object {

        fun mockSingleData(): MockData {
            return MockData("", "")
        }

        fun mockListData(times: Int = 1): MutableList<MockData> {
            return ArrayList<MockData>().apply {
                repeat(times){
                    add(mockSingleData())
                }
            }
        }

    }

}