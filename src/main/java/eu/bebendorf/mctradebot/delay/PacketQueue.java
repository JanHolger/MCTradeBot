package eu.bebendorf.mctradebot.delay;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import eu.bebendorf.mctradebot.client.MCClient;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PacketQueue implements Queue<MinecraftPacket>, Runnable {

    private long interval;
    private int amount;
    private MCClient client;

    public PacketQueue(MCClient client){
        this(client, 1, 1000);
    }

    public PacketQueue(MCClient client, int amount, long interval){
        this.interval = interval;
        this.amount = amount;
        this.client = client;
    }

    private Queue<MinecraftPacket> queue = new LinkedBlockingQueue<>();

    public void run(){
        while(interval > 0){
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < amount; i++){
                MinecraftPacket packet = queue.poll();
                if(packet != null){
                    ClientChatPacket packet1 = (ClientChatPacket) packet;
                    client.getSession().send(packet);
                }
            }
        }
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Iterator iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public Object[] toArray(Object[] objects) {
        return queue.toArray(objects);
    }

    @Override
    public boolean add(MinecraftPacket packet) {
        return queue.add(packet);
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean addAll(Collection collection) {
        return queue.addAll(collection);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean retainAll(Collection collection) {
        return queue.retainAll(collection);
    }

    @Override
    public boolean removeAll(Collection collection) {
        return queue.removeAll(collection);
    }

    @Override
    public boolean containsAll(Collection collection) {
        return queue.containsAll(collection);
    }

    @Override
    public boolean offer(MinecraftPacket packet) {
        return queue.offer(packet);
    }

    @Override
    public MinecraftPacket remove() {
        return queue.remove();
    }

    @Override
    public MinecraftPacket poll() {
        return queue.poll();
    }

    @Override
    public MinecraftPacket element() {
        return queue.element();
    }

    @Override
    public MinecraftPacket peek() {
        return queue.peek();
    }
}
