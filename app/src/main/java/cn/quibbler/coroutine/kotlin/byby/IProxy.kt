package cn.quibbler.coroutine.kotlin.byby

interface IProxy {

    fun handle()

}

class ProxyImp : IProxy {
    override fun handle() {
    }
}


class ProxyBase : IProxy by ProxyImp()