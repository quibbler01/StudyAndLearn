package cn.quibbler.coroutine.kotlin.byby

val name: String by lazy {
    "Quibbler"
}

interface IProxy {

    fun handle()

}

class ProxyImp : IProxy {
    override fun handle() {
    }
}


class ProxyBase : IProxy by ProxyImp()