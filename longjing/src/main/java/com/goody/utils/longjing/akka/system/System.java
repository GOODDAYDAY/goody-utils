package com.goody.utils.longjing.akka.system;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Terminated;
import akka.actor.typed.javadsl.Behaviors;
import akka.cluster.typed.Cluster;
import akka.cluster.typed.ClusterSingleton;
import akka.cluster.typed.SingletonActor;
import com.goody.utils.longjing.akka.actor.TaskActor;
import com.goody.utils.longjing.akka.actor.TaskBActor;
import com.goody.utils.longjing.akka.base.TaskCommand;
import com.goody.utils.longjing.akka.service.ITaskActorService;
import com.goody.utils.longjing.akka.util.AkkaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * system component
 *
 * @author Goody
 * @version 1.0, 2022/8/31 14:31
 * @since 1.0.0
 */
@Component
public class System {

    @Autowired
    private ActorSystem<Void> goodyActorSystem;
    @Autowired
    private ClusterSingleton goodyActorSystemClusterSingleton;

    @Bean("goodyActorSystem")
    public ActorSystem<Void> actorSystem() {
        return ActorSystem.create(Behaviors.setup(
            context -> Behaviors
                .receive(Void.class)
                .onSignal(Terminated.class, sig -> Behaviors.stopped())
                .build()), "goodyActorSystem");
    }

    @Bean("goodyActorSystemCluster")
    public Cluster cluster(ActorSystem<Void> system) {
        return Cluster.get(system);
    }

    @Bean("goodyActorSystemClusterSingleton")
    public ClusterSingleton clusterSingleton(ActorSystem<Void> system) {
        return ClusterSingleton.get(system);
    }

    @Bean("taskActor")
    public ActorRef<TaskCommand> taskActor(ClusterSingleton singleton, ITaskActorService service) {
        return singleton.init(
            SingletonActor.of(AkkaUtil.create(context -> new TaskActor(context, service, "init")),
                "taskActor"));
    }

    @Bean("taskBActor")
    public ActorRef<TaskCommand> taskBActor(ClusterSingleton singleton, ITaskActorService service) {
        return singleton.init(
            SingletonActor.of(AkkaUtil.create(context -> new TaskBActor(context, service, "init")),
                "taskBActor"));
    }

    /**
     * singleton actor with taskId
     *
     * @param taskId task id
     * @return ref
     */
    public ActorRef<TaskCommand> taskActor(Long taskId, ITaskActorService service) {
        return this.goodyActorSystemClusterSingleton.init(SingletonActor.of(AkkaUtil.create(context -> new TaskBActor(context, service, "init")),
            String.format("taskActor/%s", taskId)));
    }
}
