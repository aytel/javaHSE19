package com.aytel;

public class VTestInnerClasses {
    public class Inner1 {
        public Inner1() {}
        private int x;
        protected void m() {}
    }

    public class Inner2 implements Runnable {
        public void run() {}
    }

    public class Inner3 {
        public Inner4 obj;
        public class Inner4 {}
    }
}