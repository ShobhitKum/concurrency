
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TestRentrrantRWLock {


    public static void main(String[] args) throws InterruptedException {
        TestRentrrantRWLock t = new TestRentrrantRWLock();
        Map<Integer,Integer> map = t.runExecution(200);

        Thread.sleep(5000);
        System.out.println("return RWLock:::::::"+map);
    }


    HashMap<Integer,Integer> runExecution(int threads){  //10 threads
        ExecutorService ex = Executors.newFixedThreadPool(threads);
        Util2 u = new Util2();
        u.init();
        for(int i =0;i<100000;i++){   //50 calls  - 5 calls for each not thread safe
            //System.out.println((i)%10);  0-9
            UpdateThread2 workers = new UpdateThread2(u,(i+1)%10);
            ex.execute(workers);
        }
        ex.shutdown();
        return u.getMap();
    }
}

class UpdateThread2 implements Runnable{
    Util2 u;
    int val;
    UpdateThread2(Util2 u, int val){
        this.u =u;
        this.val=val;
    }
    @Override
    public void run() {
        u.incrementVal(val,1);

    }
}

class Util2{
    public HashMap<Integer,Integer> map;
    Object obj = new Object();
    ReentrantReadWriteLock reentrant = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = reentrant.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = reentrant.writeLock();
    void init(){
        map = new HashMap<>();
    }


    void incrementVal(int k,Integer newVal){

            writeLock.lock();

            if(map.get(k)==null){
                map.put(k,1);
            }else {
                map.put(k,map.get(k)+newVal);
            }

            writeLock.unlock();
           // System.out.println(map);

    }


    Integer getVal(int key){
        Integer val=null;
        readLock.lock();
           val =  map.get(key);
            readLock.unlock();
            return val;
    }

    HashMap<Integer,Integer> getMap(){
        return map;
    }
}
