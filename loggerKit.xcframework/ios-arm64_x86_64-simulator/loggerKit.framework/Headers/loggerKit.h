#import <Foundation/NSArray.h>
#import <Foundation/NSDictionary.h>
#import <Foundation/NSError.h>
#import <Foundation/NSObject.h>
#import <Foundation/NSSet.h>
#import <Foundation/NSString.h>
#import <Foundation/NSValue.h>

@class LoggerKitKotlinThrowable, LoggerKitLoggerCompanion, LoggerKitKotlinEnumCompanion, LoggerKitKotlinEnum<E>, LoggerKitLoggerLevel, LoggerKitKotlinArray<T>;

@protocol LoggerKitLogger, LoggerKitKotlinComparable, LoggerKitKotlinIterator;

NS_ASSUME_NONNULL_BEGIN
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunknown-warning-option"
#pragma clang diagnostic ignored "-Wincompatible-property-type"
#pragma clang diagnostic ignored "-Wnullability"

#pragma push_macro("_Nullable_result")
#if !__has_feature(nullability_nullable_result)
#undef _Nullable_result
#define _Nullable_result _Nullable
#endif

__attribute__((swift_name("KotlinBase")))
@interface LoggerKitBase : NSObject
- (instancetype)init __attribute__((unavailable));
+ (instancetype)new __attribute__((unavailable));
+ (void)initialize __attribute__((objc_requires_super));
@end

@interface LoggerKitBase (LoggerKitBaseCopying) <NSCopying>
@end

__attribute__((swift_name("KotlinMutableSet")))
@interface LoggerKitMutableSet<ObjectType> : NSMutableSet<ObjectType>
@end

__attribute__((swift_name("KotlinMutableDictionary")))
@interface LoggerKitMutableDictionary<KeyType, ObjectType> : NSMutableDictionary<KeyType, ObjectType>
@end

@interface NSError (NSErrorLoggerKitKotlinException)
@property (readonly) id _Nullable kotlinException;
@end

__attribute__((swift_name("KotlinNumber")))
@interface LoggerKitNumber : NSNumber
- (instancetype)initWithChar:(char)value __attribute__((unavailable));
- (instancetype)initWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
- (instancetype)initWithShort:(short)value __attribute__((unavailable));
- (instancetype)initWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
- (instancetype)initWithInt:(int)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
- (instancetype)initWithLong:(long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
- (instancetype)initWithLongLong:(long long)value __attribute__((unavailable));
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
- (instancetype)initWithFloat:(float)value __attribute__((unavailable));
- (instancetype)initWithDouble:(double)value __attribute__((unavailable));
- (instancetype)initWithBool:(BOOL)value __attribute__((unavailable));
- (instancetype)initWithInteger:(NSInteger)value __attribute__((unavailable));
- (instancetype)initWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
+ (instancetype)numberWithChar:(char)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedChar:(unsigned char)value __attribute__((unavailable));
+ (instancetype)numberWithShort:(short)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedShort:(unsigned short)value __attribute__((unavailable));
+ (instancetype)numberWithInt:(int)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInt:(unsigned int)value __attribute__((unavailable));
+ (instancetype)numberWithLong:(long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLong:(unsigned long)value __attribute__((unavailable));
+ (instancetype)numberWithLongLong:(long long)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value __attribute__((unavailable));
+ (instancetype)numberWithFloat:(float)value __attribute__((unavailable));
+ (instancetype)numberWithDouble:(double)value __attribute__((unavailable));
+ (instancetype)numberWithBool:(BOOL)value __attribute__((unavailable));
+ (instancetype)numberWithInteger:(NSInteger)value __attribute__((unavailable));
+ (instancetype)numberWithUnsignedInteger:(NSUInteger)value __attribute__((unavailable));
@end

__attribute__((swift_name("KotlinByte")))
@interface LoggerKitByte : LoggerKitNumber
- (instancetype)initWithChar:(char)value;
+ (instancetype)numberWithChar:(char)value;
@end

__attribute__((swift_name("KotlinUByte")))
@interface LoggerKitUByte : LoggerKitNumber
- (instancetype)initWithUnsignedChar:(unsigned char)value;
+ (instancetype)numberWithUnsignedChar:(unsigned char)value;
@end

__attribute__((swift_name("KotlinShort")))
@interface LoggerKitShort : LoggerKitNumber
- (instancetype)initWithShort:(short)value;
+ (instancetype)numberWithShort:(short)value;
@end

__attribute__((swift_name("KotlinUShort")))
@interface LoggerKitUShort : LoggerKitNumber
- (instancetype)initWithUnsignedShort:(unsigned short)value;
+ (instancetype)numberWithUnsignedShort:(unsigned short)value;
@end

__attribute__((swift_name("KotlinInt")))
@interface LoggerKitInt : LoggerKitNumber
- (instancetype)initWithInt:(int)value;
+ (instancetype)numberWithInt:(int)value;
@end

__attribute__((swift_name("KotlinUInt")))
@interface LoggerKitUInt : LoggerKitNumber
- (instancetype)initWithUnsignedInt:(unsigned int)value;
+ (instancetype)numberWithUnsignedInt:(unsigned int)value;
@end

__attribute__((swift_name("KotlinLong")))
@interface LoggerKitLong : LoggerKitNumber
- (instancetype)initWithLongLong:(long long)value;
+ (instancetype)numberWithLongLong:(long long)value;
@end

__attribute__((swift_name("KotlinULong")))
@interface LoggerKitULong : LoggerKitNumber
- (instancetype)initWithUnsignedLongLong:(unsigned long long)value;
+ (instancetype)numberWithUnsignedLongLong:(unsigned long long)value;
@end

__attribute__((swift_name("KotlinFloat")))
@interface LoggerKitFloat : LoggerKitNumber
- (instancetype)initWithFloat:(float)value;
+ (instancetype)numberWithFloat:(float)value;
@end

__attribute__((swift_name("KotlinDouble")))
@interface LoggerKitDouble : LoggerKitNumber
- (instancetype)initWithDouble:(double)value;
+ (instancetype)numberWithDouble:(double)value;
@end

__attribute__((swift_name("KotlinBoolean")))
@interface LoggerKitBoolean : LoggerKitNumber
- (instancetype)initWithBool:(BOOL)value;
+ (instancetype)numberWithBool:(BOOL)value;
@end

__attribute__((swift_name("Logger")))
@protocol LoggerKitLogger
@required
- (void)dTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("d(tag:msg:tr:)")));
- (void)eTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("e(tag:msg:tr:)")));
- (void)iTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("i(tag:msg:tr:)")));
- (void)wTag:(NSString *)tag tr:(LoggerKitKotlinThrowable *)tr __attribute__((swift_name("w(tag:tr:)")));
- (void)wTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("w(tag:msg:tr:)")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LoggerCompanion")))
@interface LoggerKitLoggerCompanion : LoggerKitBase <LoggerKitLogger>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) LoggerKitLoggerCompanion *shared __attribute__((swift_name("shared")));
- (void)dTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("d(tag:msg:tr:)")));
- (void)eTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("e(tag:msg:tr:)")));
- (void)iTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("i(tag:msg:tr:)")));
- (void)wTag:(NSString *)tag tr:(LoggerKitKotlinThrowable *)tr __attribute__((swift_name("w(tag:tr:)")));
- (void)wTag:(NSString *)tag msg:(NSString *)msg tr:(LoggerKitKotlinThrowable * _Nullable)tr __attribute__((swift_name("w(tag:msg:tr:)")));

/**
 * @note annotations
 *   kotlin.jvm.JvmStatic
*/
@property (readonly) id<LoggerKitLogger> SYSTEM __attribute__((swift_name("SYSTEM")));

/**
 * @note annotations
 *   kotlin.jvm.JvmStatic
*/
@property (getter=default, setter=setDefault:) id<LoggerKitLogger> default_ __attribute__((swift_name("default_")));
@end

__attribute__((swift_name("KotlinComparable")))
@protocol LoggerKitKotlinComparable
@required
- (int32_t)compareToOther:(id _Nullable)other __attribute__((swift_name("compareTo(other:)")));
@end

__attribute__((swift_name("KotlinEnum")))
@interface LoggerKitKotlinEnum<E> : LoggerKitBase <LoggerKitKotlinComparable>
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer));
@property (class, readonly, getter=companion) LoggerKitKotlinEnumCompanion *companion __attribute__((swift_name("companion")));
- (int32_t)compareToOther:(E)other __attribute__((swift_name("compareTo(other:)")));
- (BOOL)isEqual:(id _Nullable)other __attribute__((swift_name("isEqual(_:)")));
- (NSUInteger)hash __attribute__((swift_name("hash()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) NSString *name __attribute__((swift_name("name")));
@property (readonly) int32_t ordinal __attribute__((swift_name("ordinal")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("LoggerLevel")))
@interface LoggerKitLoggerLevel : LoggerKitKotlinEnum<LoggerKitLoggerLevel *>
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (instancetype)initWithName:(NSString *)name ordinal:(int32_t)ordinal __attribute__((swift_name("init(name:ordinal:)"))) __attribute__((objc_designated_initializer)) __attribute__((unavailable));
@property (class, readonly) LoggerKitLoggerLevel *debug __attribute__((swift_name("debug")));
@property (class, readonly) LoggerKitLoggerLevel *info __attribute__((swift_name("info")));
@property (class, readonly) LoggerKitLoggerLevel *warn __attribute__((swift_name("warn")));
@property (class, readonly) LoggerKitLoggerLevel *error __attribute__((swift_name("error")));
+ (LoggerKitKotlinArray<LoggerKitLoggerLevel *> *)values __attribute__((swift_name("values()")));
@property (class, readonly) NSArray<LoggerKitLoggerLevel *> *entries __attribute__((swift_name("entries")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("CompositeLoggerKt")))
@interface LoggerKitCompositeLoggerKt : LoggerKitBase

/**
 * @note annotations
 *   kotlin.jvm.JvmName(name="combine")
*/
+ (id<LoggerKitLogger>)plus:(id<LoggerKitLogger>)receiver other:(id<LoggerKitLogger>)other __attribute__((swift_name("plus(_:other:)")));
@end

__attribute__((swift_name("KotlinThrowable")))
@interface LoggerKitKotlinThrowable : LoggerKitBase
- (instancetype)init __attribute__((swift_name("init()"))) __attribute__((objc_designated_initializer));
+ (instancetype)new __attribute__((availability(swift, unavailable, message="use object initializers instead")));
- (instancetype)initWithMessage:(NSString * _Nullable)message __attribute__((swift_name("init(message:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithCause:(LoggerKitKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(cause:)"))) __attribute__((objc_designated_initializer));
- (instancetype)initWithMessage:(NSString * _Nullable)message cause:(LoggerKitKotlinThrowable * _Nullable)cause __attribute__((swift_name("init(message:cause:)"))) __attribute__((objc_designated_initializer));

/**
 * @note annotations
 *   kotlin.experimental.ExperimentalNativeApi
*/
- (LoggerKitKotlinArray<NSString *> *)getStackTrace __attribute__((swift_name("getStackTrace()")));
- (void)printStackTrace __attribute__((swift_name("printStackTrace()")));
- (NSString *)description __attribute__((swift_name("description()")));
@property (readonly) LoggerKitKotlinThrowable * _Nullable cause __attribute__((swift_name("cause")));
@property (readonly) NSString * _Nullable message __attribute__((swift_name("message")));
- (NSError *)asError __attribute__((swift_name("asError()")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinEnumCompanion")))
@interface LoggerKitKotlinEnumCompanion : LoggerKitBase
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
+ (instancetype)companion __attribute__((swift_name("init()")));
@property (class, readonly, getter=shared) LoggerKitKotlinEnumCompanion *shared __attribute__((swift_name("shared")));
@end

__attribute__((objc_subclassing_restricted))
__attribute__((swift_name("KotlinArray")))
@interface LoggerKitKotlinArray<T> : LoggerKitBase
+ (instancetype)arrayWithSize:(int32_t)size init:(T _Nullable (^)(LoggerKitInt *))init __attribute__((swift_name("init(size:init:)")));
+ (instancetype)alloc __attribute__((unavailable));
+ (instancetype)allocWithZone:(struct _NSZone *)zone __attribute__((unavailable));
- (T _Nullable)getIndex:(int32_t)index __attribute__((swift_name("get(index:)")));
- (id<LoggerKitKotlinIterator>)iterator __attribute__((swift_name("iterator()")));
- (void)setIndex:(int32_t)index value:(T _Nullable)value __attribute__((swift_name("set(index:value:)")));
@property (readonly) int32_t size __attribute__((swift_name("size")));
@end

__attribute__((swift_name("KotlinIterator")))
@protocol LoggerKitKotlinIterator
@required
- (BOOL)hasNext __attribute__((swift_name("hasNext()")));
- (id _Nullable)next __attribute__((swift_name("next()")));
@end

#pragma pop_macro("_Nullable_result")
#pragma clang diagnostic pop
NS_ASSUME_NONNULL_END
