

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Test t = new Test();
        Map<Integer,Integer> map = t.runExecution(20);

        Thread.sleep(5000);
        System.out.println("return:::::::"+map);
    }


    ConcurrentHashMap<Integer,Integer> runExecution(int threads){  //10 threads
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        Util u = new Util();
        u.init();
        for(int i =0;i<100;i++){   //50 calls  - 5 calls for each not thread safe
            //System.out.println((i)%10);  0-9
            UpdateThread workers = new UpdateThread(u,(i+1)%10);
            ex.execute(workers);
        }
        ex.shutdown();
        return u.getMap();
    }
}

class UpdateThread implements Runnable{
    Util u;
    int val;
    UpdateThread(Util u, int val){
        this.u =u;
        this.val=val;
    }
    @Override
    public void run() {
      // if(u.getVal(val)==null){
           u.setMap(val);
      // }else {
        //   u.setMap(val,u.getVal(val)+1);
      // }
    }
}

class Util{
    public ConcurrentHashMap<Integer,Integer> map;
    Object obj = new Object();
    void init(){
        map = new ConcurrentHashMap<>();
    }


    void setMap(int k){
        try {


        synchronized (obj) {
            map.compute(k, (key, value) -> value == null ? 1 : value + 1);

           // map.compute(key,(k,v)->v==null?:1,v+1);
            //map.put(key, val);
            System.out.println(map);
        }
        }catch (Exception e){
            System.out.println(e);
        }

    }


    Integer getVal(int key){
        synchronized (obj) {
            return map.get(key);
        }
    }

    ConcurrentHashMap<Integer,Integer> getMap(){
        return map;
    }
}
