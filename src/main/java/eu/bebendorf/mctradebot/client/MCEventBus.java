package eu.bebendorf.mctradebot.client;

import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class MCEventBus {

    private MCClient client;
    private Map<Object, Map<Class<?>, List<Method>>> listeners = new HashMap<>();

    public MCEventBus(MCClient client){
        this.client = client;
        client.getSession().addListener(new SessionAdapter() {
            public void packetReceived(PacketReceivedEvent event) {
                fire(event.getPacket());
            }
        });
    }

    public void fire(Object event){
        if(client == null)
            return;
        for(Object l : listeners.keySet()){
            Map<Class<?>, List<Method>> mapping = listeners.get(l);
            List<Method> methods = mapping.get(event.getClass());
            if(methods != null){
                for(Method method : methods){
                    try {
                        method.invoke(l, client, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void register(Object object){
        if(client == null)
            return;
        Map<Class<?>, List<Method>> mapping = new HashMap<>();
        for(Method method : object.getClass().getDeclaredMethods()){
            method.setAccessible(true);
            if(Modifier.isStatic(method.getModifiers()))
                continue;
            if(!method.isAnnotationPresent(MCEvent.class))
                continue;
            if(method.getParameterCount() != 2)
                continue;
            if(!method.getParameterTypes()[0].equals(MCClient.class))
                continue;
            Class<?> packetType = method.getParameterTypes()[1];
            List<Method> methods = mapping.get(packetType);
            if(methods == null)
                methods = new ArrayList<>();
            methods.add(method);
            mapping.put(packetType, methods);
        }
        listeners.put(object, mapping);
    }

    public void unregister(Object object){
        listeners.remove(object);
    }

    public void close(){
        listeners.clear();
        client = null;
    }

}
