package cn.quibbler.coroutine.github.otto;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

public class TestOtto {

    private Bus bus = new Bus();

    public void fun() {
        Bus bus = new Bus("Quibbler");

        bus.register(this);

        bus.post(new Event());

        bus.toString();

        bus.unregister(this);
    }

    public static class Event {
        //something
    }

    @Subscribe
    public void onEvent(Event event) {
        //todo
    }

}
