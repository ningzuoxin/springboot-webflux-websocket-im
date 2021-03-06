// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ClientMessage.proto

package com.ning.message;

public final class ClientMessage {
  private ClientMessage() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * Protobuf enum {@code ClientMessage.proto.Type}
   */
  public enum Type
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <code>NULL = 0;</code>
     */
    NULL(0),
    /**
     * <code>COMMAND = 1;</code>
     */
    COMMAND(1),
    /**
     * <code>BIND_PHONE_NO = 2;</code>
     */
    BIND_PHONE_NO(2),
    /**
     * <code>BIND_CUSTOM_NO = 3;</code>
     */
    BIND_CUSTOM_NO(3),
    /**
     * <code>BUSINESS = 4;</code>
     */
    BUSINESS(4),
    /**
     * <code>CLOSE = 5;</code>
     */
    CLOSE(5),
    UNRECOGNIZED(-1),
    ;

    /**
     * <code>NULL = 0;</code>
     */
    public static final int NULL_VALUE = 0;
    /**
     * <code>COMMAND = 1;</code>
     */
    public static final int COMMAND_VALUE = 1;
    /**
     * <code>BIND_PHONE_NO = 2;</code>
     */
    public static final int BIND_PHONE_NO_VALUE = 2;
    /**
     * <code>BIND_CUSTOM_NO = 3;</code>
     */
    public static final int BIND_CUSTOM_NO_VALUE = 3;
    /**
     * <code>BUSINESS = 4;</code>
     */
    public static final int BUSINESS_VALUE = 4;
    /**
     * <code>CLOSE = 5;</code>
     */
    public static final int CLOSE_VALUE = 5;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new java.lang.IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @java.lang.Deprecated
    public static Type valueOf(int value) {
      return forNumber(value);
    }

    public static Type forNumber(int value) {
      switch (value) {
        case 0: return NULL;
        case 1: return COMMAND;
        case 2: return BIND_PHONE_NO;
        case 3: return BIND_CUSTOM_NO;
        case 4: return BUSINESS;
        case 5: return CLOSE;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<Type>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        Type> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<Type>() {
            public Type findValueByNumber(int number) {
              return Type.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return com.ning.message.ClientMessage.getDescriptor().getEnumTypes().get(0);
    }

    private static final Type[] VALUES = values();

    public static Type valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private Type(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:ClientMessage.proto.Type)
  }

  public interface MessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:ClientMessage.proto.Message)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .ClientMessage.proto.Type type = 1;</code>
     */
    int getTypeValue();
    /**
     * <code>optional .ClientMessage.proto.Type type = 1;</code>
     */
    com.ning.message.ClientMessage.Type getType();

    /**
     * <code>optional uint64 timestamp = 2;</code>
     */
    long getTimestamp();

    /**
     * <code>optional uint32 business = 3;</code>
     */
    int getBusiness();

    /**
     * <code>optional string payload = 4;</code>
     */
    java.lang.String getPayload();
    /**
     * <code>optional string payload = 4;</code>
     */
    com.google.protobuf.ByteString
        getPayloadBytes();
  }
  /**
   * Protobuf type {@code ClientMessage.proto.Message}
   */
  public  static final class Message extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:ClientMessage.proto.Message)
      MessageOrBuilder {
    // Use Message.newBuilder() to construct.
    private Message(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Message() {
      type_ = 0;
      timestamp_ = 0L;
      business_ = 0;
      payload_ = "";
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private Message(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              int rawValue = input.readEnum();

              type_ = rawValue;
              break;
            }
            case 16: {

              timestamp_ = input.readUInt64();
              break;
            }
            case 24: {

              business_ = input.readUInt32();
              break;
            }
            case 34: {
              java.lang.String s = input.readStringRequireUtf8();

              payload_ = s;
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.ning.message.ClientMessage.internal_static_ClientMessage_proto_Message_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.ning.message.ClientMessage.internal_static_ClientMessage_proto_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.ning.message.ClientMessage.Message.class, com.ning.message.ClientMessage.Message.Builder.class);
    }

    public static final int TYPE_FIELD_NUMBER = 1;
    private int type_;
    /**
     * <code>optional .ClientMessage.proto.Type type = 1;</code>
     */
    public int getTypeValue() {
      return type_;
    }
    /**
     * <code>optional .ClientMessage.proto.Type type = 1;</code>
     */
    public com.ning.message.ClientMessage.Type getType() {
      com.ning.message.ClientMessage.Type result = com.ning.message.ClientMessage.Type.valueOf(type_);
      return result == null ? com.ning.message.ClientMessage.Type.UNRECOGNIZED : result;
    }

    public static final int TIMESTAMP_FIELD_NUMBER = 2;
    private long timestamp_;
    /**
     * <code>optional uint64 timestamp = 2;</code>
     */
    public long getTimestamp() {
      return timestamp_;
    }

    public static final int BUSINESS_FIELD_NUMBER = 3;
    private int business_;
    /**
     * <code>optional uint32 business = 3;</code>
     */
    public int getBusiness() {
      return business_;
    }

    public static final int PAYLOAD_FIELD_NUMBER = 4;
    private volatile java.lang.Object payload_;
    /**
     * <code>optional string payload = 4;</code>
     */
    public java.lang.String getPayload() {
      java.lang.Object ref = payload_;
      if (ref instanceof java.lang.String) {
        return (java.lang.String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        payload_ = s;
        return s;
      }
    }
    /**
     * <code>optional string payload = 4;</code>
     */
    public com.google.protobuf.ByteString
        getPayloadBytes() {
      java.lang.Object ref = payload_;
      if (ref instanceof java.lang.String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        payload_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (type_ != com.ning.message.ClientMessage.Type.NULL.getNumber()) {
        output.writeEnum(1, type_);
      }
      if (timestamp_ != 0L) {
        output.writeUInt64(2, timestamp_);
      }
      if (business_ != 0) {
        output.writeUInt32(3, business_);
      }
      if (!getPayloadBytes().isEmpty()) {
        com.google.protobuf.GeneratedMessageV3.writeString(output, 4, payload_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (type_ != com.ning.message.ClientMessage.Type.NULL.getNumber()) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(1, type_);
      }
      if (timestamp_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(2, timestamp_);
      }
      if (business_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(3, business_);
      }
      if (!getPayloadBytes().isEmpty()) {
        size += com.google.protobuf.GeneratedMessageV3.computeStringSize(4, payload_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof com.ning.message.ClientMessage.Message)) {
        return super.equals(obj);
      }
      com.ning.message.ClientMessage.Message other = (com.ning.message.ClientMessage.Message) obj;

      boolean result = true;
      result = result && type_ == other.type_;
      result = result && (getTimestamp()
          == other.getTimestamp());
      result = result && (getBusiness()
          == other.getBusiness());
      result = result && getPayload()
          .equals(other.getPayload());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      hash = (37 * hash) + TYPE_FIELD_NUMBER;
      hash = (53 * hash) + type_;
      hash = (37 * hash) + TIMESTAMP_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTimestamp());
      hash = (37 * hash) + BUSINESS_FIELD_NUMBER;
      hash = (53 * hash) + getBusiness();
      hash = (37 * hash) + PAYLOAD_FIELD_NUMBER;
      hash = (53 * hash) + getPayload().hashCode();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static com.ning.message.ClientMessage.Message parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.ning.message.ClientMessage.Message parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.ning.message.ClientMessage.Message parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static com.ning.message.ClientMessage.Message parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static com.ning.message.ClientMessage.Message parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.ning.message.ClientMessage.Message parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.ning.message.ClientMessage.Message parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static com.ning.message.ClientMessage.Message parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static com.ning.message.ClientMessage.Message parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static com.ning.message.ClientMessage.Message parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.ning.message.ClientMessage.Message prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code ClientMessage.proto.Message}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:ClientMessage.proto.Message)
        com.ning.message.ClientMessage.MessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.ning.message.ClientMessage.internal_static_ClientMessage_proto_Message_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.ning.message.ClientMessage.internal_static_ClientMessage_proto_Message_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                com.ning.message.ClientMessage.Message.class, com.ning.message.ClientMessage.Message.Builder.class);
      }

      // Construct using com.ning.message.ClientMessage.Message.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        type_ = 0;

        timestamp_ = 0L;

        business_ = 0;

        payload_ = "";

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.ning.message.ClientMessage.internal_static_ClientMessage_proto_Message_descriptor;
      }

      public com.ning.message.ClientMessage.Message getDefaultInstanceForType() {
        return com.ning.message.ClientMessage.Message.getDefaultInstance();
      }

      public com.ning.message.ClientMessage.Message build() {
        com.ning.message.ClientMessage.Message result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public com.ning.message.ClientMessage.Message buildPartial() {
        com.ning.message.ClientMessage.Message result = new com.ning.message.ClientMessage.Message(this);
        result.type_ = type_;
        result.timestamp_ = timestamp_;
        result.business_ = business_;
        result.payload_ = payload_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.ning.message.ClientMessage.Message) {
          return mergeFrom((com.ning.message.ClientMessage.Message)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.ning.message.ClientMessage.Message other) {
        if (other == com.ning.message.ClientMessage.Message.getDefaultInstance()) return this;
        if (other.type_ != 0) {
          setTypeValue(other.getTypeValue());
        }
        if (other.getTimestamp() != 0L) {
          setTimestamp(other.getTimestamp());
        }
        if (other.getBusiness() != 0) {
          setBusiness(other.getBusiness());
        }
        if (!other.getPayload().isEmpty()) {
          payload_ = other.payload_;
          onChanged();
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.ning.message.ClientMessage.Message parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (com.ning.message.ClientMessage.Message) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int type_ = 0;
      /**
       * <code>optional .ClientMessage.proto.Type type = 1;</code>
       */
      public int getTypeValue() {
        return type_;
      }
      /**
       * <code>optional .ClientMessage.proto.Type type = 1;</code>
       */
      public Builder setTypeValue(int value) {
        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional .ClientMessage.proto.Type type = 1;</code>
       */
      public com.ning.message.ClientMessage.Type getType() {
        com.ning.message.ClientMessage.Type result = com.ning.message.ClientMessage.Type.valueOf(type_);
        return result == null ? com.ning.message.ClientMessage.Type.UNRECOGNIZED : result;
      }
      /**
       * <code>optional .ClientMessage.proto.Type type = 1;</code>
       */
      public Builder setType(com.ning.message.ClientMessage.Type value) {
        if (value == null) {
          throw new NullPointerException();
        }
        
        type_ = value.getNumber();
        onChanged();
        return this;
      }
      /**
       * <code>optional .ClientMessage.proto.Type type = 1;</code>
       */
      public Builder clearType() {
        
        type_ = 0;
        onChanged();
        return this;
      }

      private long timestamp_ ;
      /**
       * <code>optional uint64 timestamp = 2;</code>
       */
      public long getTimestamp() {
        return timestamp_;
      }
      /**
       * <code>optional uint64 timestamp = 2;</code>
       */
      public Builder setTimestamp(long value) {
        
        timestamp_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 timestamp = 2;</code>
       */
      public Builder clearTimestamp() {
        
        timestamp_ = 0L;
        onChanged();
        return this;
      }

      private int business_ ;
      /**
       * <code>optional uint32 business = 3;</code>
       */
      public int getBusiness() {
        return business_;
      }
      /**
       * <code>optional uint32 business = 3;</code>
       */
      public Builder setBusiness(int value) {
        
        business_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 business = 3;</code>
       */
      public Builder clearBusiness() {
        
        business_ = 0;
        onChanged();
        return this;
      }

      private java.lang.Object payload_ = "";
      /**
       * <code>optional string payload = 4;</code>
       */
      public java.lang.String getPayload() {
        java.lang.Object ref = payload_;
        if (!(ref instanceof java.lang.String)) {
          com.google.protobuf.ByteString bs =
              (com.google.protobuf.ByteString) ref;
          java.lang.String s = bs.toStringUtf8();
          payload_ = s;
          return s;
        } else {
          return (java.lang.String) ref;
        }
      }
      /**
       * <code>optional string payload = 4;</code>
       */
      public com.google.protobuf.ByteString
          getPayloadBytes() {
        java.lang.Object ref = payload_;
        if (ref instanceof String) {
          com.google.protobuf.ByteString b = 
              com.google.protobuf.ByteString.copyFromUtf8(
                  (java.lang.String) ref);
          payload_ = b;
          return b;
        } else {
          return (com.google.protobuf.ByteString) ref;
        }
      }
      /**
       * <code>optional string payload = 4;</code>
       */
      public Builder setPayload(
          java.lang.String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        payload_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional string payload = 4;</code>
       */
      public Builder clearPayload() {
        
        payload_ = getDefaultInstance().getPayload();
        onChanged();
        return this;
      }
      /**
       * <code>optional string payload = 4;</code>
       */
      public Builder setPayloadBytes(
          com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
        
        payload_ = value;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:ClientMessage.proto.Message)
    }

    // @@protoc_insertion_point(class_scope:ClientMessage.proto.Message)
    private static final com.ning.message.ClientMessage.Message DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new com.ning.message.ClientMessage.Message();
    }

    public static com.ning.message.ClientMessage.Message getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Message>
        PARSER = new com.google.protobuf.AbstractParser<Message>() {
      public Message parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new Message(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Message> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Message> getParserForType() {
      return PARSER;
    }

    public com.ning.message.ClientMessage.Message getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_ClientMessage_proto_Message_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_ClientMessage_proto_Message_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023ClientMessage.proto\022\023ClientMessage.pro" +
      "to\"h\n\007Message\022\'\n\004type\030\001 \001(\0162\031.ClientMess" +
      "age.proto.Type\022\021\n\ttimestamp\030\002 \001(\004\022\020\n\010bus" +
      "iness\030\003 \001(\r\022\017\n\007payload\030\004 \001(\t*]\n\004Type\022\010\n\004" +
      "NULL\020\000\022\013\n\007COMMAND\020\001\022\021\n\rBIND_PHONE_NO\020\002\022\022" +
      "\n\016BIND_CUSTOM_NO\020\003\022\014\n\010BUSINESS\020\004\022\t\n\005CLOS" +
      "E\020\005B!\n\020com.ning.messageB\rClientMessageb\006" +
      "proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_ClientMessage_proto_Message_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_ClientMessage_proto_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_ClientMessage_proto_Message_descriptor,
        new java.lang.String[] { "Type", "Timestamp", "Business", "Payload", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
