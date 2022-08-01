package com.goody.utils.maojian;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 *
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.48.0)",
    comments = "Source: Toy.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class PeopleServiceGrpc {

  public static final String SERVICE_NAME = "com.goody.utils.maojian.PeopleService";
  private static final int METHODID_HELLO = 0;
  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.goody.utils.maojian.People,
      com.goody.utils.maojian.People> getHelloMethod;
  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  private PeopleServiceGrpc() {}

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "hello",
      requestType = com.goody.utils.maojian.People.class,
      responseType = com.goody.utils.maojian.People.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.goody.utils.maojian.People,
      com.goody.utils.maojian.People> getHelloMethod() {
    io.grpc.MethodDescriptor<com.goody.utils.maojian.People, com.goody.utils.maojian.People> getHelloMethod;
    if ((getHelloMethod = PeopleServiceGrpc.getHelloMethod) == null) {
      synchronized (PeopleServiceGrpc.class) {
        if ((getHelloMethod = PeopleServiceGrpc.getHelloMethod) == null) {
          PeopleServiceGrpc.getHelloMethod = getHelloMethod =
              io.grpc.MethodDescriptor.<com.goody.utils.maojian.People, com.goody.utils.maojian.People>newBuilder()
                  .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                  .setFullMethodName(generateFullMethodName(SERVICE_NAME, "hello"))
                  .setSampledToLocalTracing(true)
                  .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                      com.goody.utils.maojian.People.getDefaultInstance()))
                  .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                      com.goody.utils.maojian.People.getDefaultInstance()))
                  .setSchemaDescriptor(new PeopleServiceMethodDescriptorSupplier("hello"))
                  .build();
        }
      }
    }
    return getHelloMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PeopleServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeopleServiceStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<PeopleServiceStub>() {
          @java.lang.Override
          public PeopleServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new PeopleServiceStub(channel, callOptions);
          }
        };
    return PeopleServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PeopleServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeopleServiceBlockingStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<PeopleServiceBlockingStub>() {
          @java.lang.Override
          public PeopleServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new PeopleServiceBlockingStub(channel, callOptions);
          }
        };
    return PeopleServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PeopleServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PeopleServiceFutureStub> factory =
        new io.grpc.stub.AbstractStub.StubFactory<PeopleServiceFutureStub>() {
          @java.lang.Override
          public PeopleServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
            return new PeopleServiceFutureStub(channel, callOptions);
          }
        };
    return PeopleServiceFutureStub.newStub(factory, channel);
  }

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PeopleServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PeopleServiceFileDescriptorSupplier())
              .addMethod(getHelloMethod())
              .build();
        }
      }
    }
    return result;
  }

  /**
   *
   */
  public static abstract class PeopleServiceImplBase implements io.grpc.BindableService {

    /**
     *
     */
    public void hello(com.goody.utils.maojian.People request,
                      io.grpc.stub.StreamObserver<com.goody.utils.maojian.People> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHelloMethod(), responseObserver);
    }

    @java.lang.Override
    public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
              getHelloMethod(),
              io.grpc.stub.ServerCalls.asyncUnaryCall(
                  new MethodHandlers<
                      com.goody.utils.maojian.People,
                      com.goody.utils.maojian.People>(
                      this, METHODID_HELLO)))
          .build();
    }
  }

  /**
   *
   */
  public static final class PeopleServiceStub extends io.grpc.stub.AbstractAsyncStub<PeopleServiceStub> {
    private PeopleServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeopleServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeopleServiceStub(channel, callOptions);
    }

    /**
     *
     */
    public void hello(com.goody.utils.maojian.People request,
                      io.grpc.stub.StreamObserver<com.goody.utils.maojian.People> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   *
   */
  public static final class PeopleServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<PeopleServiceBlockingStub> {
    private PeopleServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeopleServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeopleServiceBlockingStub(channel, callOptions);
    }

    /**
     *
     */
    public com.goody.utils.maojian.People hello(com.goody.utils.maojian.People request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   *
   */
  public static final class PeopleServiceFutureStub extends io.grpc.stub.AbstractFutureStub<PeopleServiceFutureStub> {
    private PeopleServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeopleServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PeopleServiceFutureStub(channel, callOptions);
    }

    /**
     *
     */
    public com.google.common.util.concurrent.ListenableFuture<com.goody.utils.maojian.People> hello(
        com.goody.utils.maojian.People request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request);
    }
  }

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PeopleServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PeopleServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HELLO:
          serviceImpl.hello((com.goody.utils.maojian.People) request,
              (io.grpc.stub.StreamObserver<com.goody.utils.maojian.People>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PeopleServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PeopleServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.goody.utils.maojian.Toy.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PeopleService");
    }
  }

  private static final class PeopleServiceFileDescriptorSupplier
      extends PeopleServiceBaseDescriptorSupplier {
    PeopleServiceFileDescriptorSupplier() {}
  }

  private static final class PeopleServiceMethodDescriptorSupplier
      extends PeopleServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PeopleServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }
}
