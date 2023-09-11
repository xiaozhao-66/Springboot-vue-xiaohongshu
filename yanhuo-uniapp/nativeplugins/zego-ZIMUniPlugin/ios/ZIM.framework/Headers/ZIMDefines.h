//
//  ZIMDefines.h
//  ZIM
//
//  Copyright © 2021 Zego. All rights reserved.
//

#import "ZIMErrorCode.h"
#import <Foundation/Foundation.h>

#if TARGET_OS_IPHONE
#import <UIKit/UIKit.h>
#elif TARGET_OS_OSX
#import <AppKit/AppKit.h>
#endif

NS_ASSUME_NONNULL_BEGIN

// MARK: - Enum

/// Connection state.
///
/// Description: The state machine that identifies the current connection state.
///
/// Use cases: It can be used to determine whether the login/logout is successful, and to handle abnormal situations such as network disconnection.
///
/// Caution: Please use it with the connection event parameter.
///
typedef NS_ENUM(NSUInteger, ZIMConnectionState) {

    /// Description: Unconnected state, enter this state before logging in and after logging out.
    ///
    /// Use cases: If there is a steady state abnormality in the process of logging in, such as AppID or Token are incorrect, or if the same user name is logged in elsewhere and the local end is kicked out, it will enter this state.
    ZIMConnectionStateDisconnected = 0,

    /// Description: The state that the connection is being requested. It will enter this state after successful execution login function.
    ///
    /// Use cases: The display of the UI is usually performed using this state. If the connection is interrupted due to poor network quality, the SDK will perform an internal retry and will return to this state.
    ZIMConnectionStateConnecting = 1,

    /// Description: The state that is successfully connected.
    ///
    /// Use cases: Entering this state indicates that login successfully and the user can use the SDK functions normally.
    ZIMConnectionStateConnected = 2,

    /**
     * Description: The state that the reconnection is being requested. It will enter this state after successful execution login function. 
     *
     * Use cases: The display of the UI is usually performed using this state. If the connection is interrupted due to poor network quality, the SDK will perform an internal retry and will return to this state.
     */
    ZIMConnectionStateReconnecting = 3
};

// MARK: - Enum

/// Connection state.
///
/// Description: The state machine that identifies the current connection state.
///
/// Use cases: It can be used to judge whether the user enters/exit the room successfully, and handles abnormal situations such as network disconnection.
///
/// Caution: Please use it with the connection event parameter.
///
typedef NS_ENUM(NSUInteger, ZIMRoomState) {

    /// Description: Disconnected state.
    ///
    /// Use cases: enter this state before entering the room and after exiting the room.
    ZIMRoomStateDisconnected = 0,

    /// Description: The connection state is being requested.
    ///
    /// Use cases: and it will enter this state after the action of entering the room is executed successfully. The application interface is usually displayed through this state.
    ZIMRoomStateConnecting = 1,

    /// Description: The connection is successful.
    ///
    /// Use cases: Entering this state means that the room has been successfully entered, and the user can use the room's functions normally.
    ZIMRoomStateConnected = 2

};

/// The event that caused the connection status to change.
///
/// Description: The reason for the change of the connection state.
///
/// Use cases: It can be used to determine whether the login/logout is successful, and to handle abnormal situations such as network disconnection.
///
/// Caution: Please use it with the connection state parameter.
///
typedef NS_ENUM(NSUInteger, ZIMConnectionEvent) {

    /// Description: Success.
    ZIMConnectionEventSuccess = 0,

    /// Description: The user actively logs in.
    ZIMConnectionEventActiveLogin = 1,

    /// Description: Connection timed out.
    ZIMConnectionEventLoginTimeout = 2,

    /// Description: The network connection is temporarily interrupted.
    ZIMConnectionEventLoginInterrupted = 3,

    /// Description: Being kicked out.
    ZIMConnectionEventKickedOut = 4,

    /// Being token expierd.
    ZIMConnectionEventTokenExpired = 5

};

/// The event that caused the room connection status to change.
///
/// Description: The reason for the change of the connection state.
///
/// Use cases: It can be used to determine whether the login/logout is successful, and to handle abnormal situations such as network disconnection.
///
/// Caution: Please use it with the connection state parameter.
///
typedef NS_ENUM(NSUInteger, ZIMRoomEvent) {

    /// Description: Success.
    ZIMRoomEventSuccess = 0,

    /// Description: The network in the room is temporarily interrupted.
    ZIMRoomEventNetworkInterrupted = 1,

    /// Description: The network in the room is disconnected.
    ZIMRoomEventNetworkDisconnected = 2,

    /// Description: The room not exist.
    ZIMRoomEventRoomNotExist = 3,

    /// Description: The user actively creates a room.
    ZIMRoomEventActiveCreate = 4,

    /// Description: Failed to create room.
    ZIMRoomEventCreateFailed = 5,

    /// Description: The user starts to enter the room.
    ZIMRoomEventActiveEnter = 6,

    /// Description: user failed to enter the room.
    ZIMRoomEventEnterFailed = 7,

    /// Description: user was kicked out of the room.
    ZIMRoomEventKickedOut = 8,

    /// Description: user was out of the room because of connect timeout
    ZIMRoomEventConnectTimeout = 9,

};

/// The priority of the message.
///
/// Description: Identifies the priority of a message.
///
/// Use cases: It can be used to set the priority when a message is sent. The higher the priority, the higher the reliability. Low priority may be discarded due to weak network.
///
/// Caution: The higher the priority, the higher the cost.
///
typedef NS_ENUM(NSUInteger, ZIMMessagePriority) {

    /// Description: Low priority.
    ///
    /// Use cases: Generally used to send unimportant messages such as barrage message in a room.
    ZIMMessagePriorityLow = 1,

    /// Description: Medium priority.
    ///
    /// Use cases: Generally used to send regular chat messages.
    ZIMMessagePriorityMedium = 2,

    /// Description: High priority.
    ///
    /// Use cases: Generally used to send important information such as gifts and rewards in a room.
    ZIMMessagePriorityHigh = 3

};

/// The type of the message.
///
/// Description: Identifies the type of current message.
///
/// Use cases: It can be used to determine what type of message this message is.
///
typedef NS_ENUM(NSUInteger, ZIMMessageType) {
    /// Description: Unknown message.
    ///
    /// Use cases: A message of an unknown type is received, indicating that the sender may have sent a message type that the user does not support, and the user needs to be advised to update the version.
    ZIMMessageTypeUnknown = 0,
    /// Description: Normal text message.
    ///
    /// Use cases: Can be used to deliver ordinary text messages.
    ZIMMessageTypeText = 1,
    /// Description: Custom binary message.
    ///
    /// Use cases: Can be used to transfer custom binary messages. This message type does not support offline messages and local storage.
    ZIMMessageTypeCommand = 2,
    /// Description: Image message.
    ///
    /// Use cases: Can be used to send image messages, only ".jpg", ".jpeg", ".png", ".bmp", ".gif", ".tiff" image types are supported. After sending the image, the server will generate a large image and a thumbnail of the original image.
    ZIMMessageTypeImage = 11,
    /// Description: File message.
    ///
    /// Use cases: For sending file messages, no file type restrictions.
    ZIMMessageTypeFile = 12,
    /// Description: Audio message.
    ///
    /// Use cases: For sending audio messages, only ".mp3" audio type is supported.
    ZIMMessageTypeAudio = 13,
    /// Description: Video message.
    ///
    /// Use cases: For sending video messages, only ".mp4", ".mov" video types are supported. After sending the video message, the server will generate the first frame of the video file.
    ZIMMessageTypeVideo = 14,
    /// Description: Barrage message.
    ///
    /// Use cases: Can be used for the barrage sent by the live room. This message type does not support offline messages and local storage.
    ZIMMessageTypeBarrage = 20,

    ZIMMessageTypeSystem = 30,

    ZIMMessageTypeRevoke = 31,
};

typedef NS_ENUM(NSInteger, ZIMRevokeType) {

    ZIMRevokeTypeUnknown = -1,

    ZIMRevokeTypeTwoWay = 0,

    ZIMRevokeTypeOneWay = 1,

};

typedef NS_ENUM(NSInteger, ZIMMessageRevokeStatus) {

    ZIMMessageRevokeStatusUnknown = -1,

    ZIMMessageRevokeStatusSelfRevoke = 0,

    ZIMMessageRevokeStatusSystemRevoke = 1,

    ZIMMessageRevokeStatusServerApiRevoke = 2,

    ZIMMessageRevokeStatusGroupAdminRevoke = 3,

    ZIMMessageRevokeStatusGroupOwnerRevoke = 4,

};

/// he types of media files that are allowed to be downloaded include original images, large images, thumbnails, general files, audio files, video files and their first frame images.
typedef NS_ENUM(NSUInteger, ZIMMediaFileType) {
    /// Original file type, suitable for original images, audio files, and video files. After calling [downloadMediaFile], the SDK will update the fileLocalPath property in [ZIMMediaMessage].
    ZIMMediaFileTypeOriginalFile = 1,
    /// Large image type. After calling [downloadMediaFile], the SDK will update the largeImageLocalPath property in [ZIMImageMessage].
    ZIMMediaFileTypeLargeImage = 2,
    /// Image thumbnail type. After calling [downloadMediaFile], the SDK will update the thumbnailLocalPath property in [ZIMImageMessage].
    ZIMMediaFileTypeThumbnail = 3,
    /// The type of the first frame of the video. After calling [downloadMediaFile], the SDK will update the videoFirstFrameLocalPath property in [ZIMVideoMessage].
    ZIMMediaFileTypeVideoFirstFrame = 4
};

typedef NS_ENUM(NSUInteger, ZIMMessageReceiptStatus) {
    ZIMMessageReceiptStatusNone = 0,
    ZIMMessageReceiptStatusProcessing = 1,
    ZIMMessageReceiptStatusDone = 2,
    ZIMMessageReceiptStatusExpired = 3,
    ZIMMessageReceiptStatusFailed = 4,
};

/// Room attributes update action.
///
/// Description: Room attributes update action.
typedef NS_ENUM(NSUInteger, ZIMRoomAttributesUpdateAction) {
    /// Set action.
    ZIMRoomAttributesUpdateActionSet = 0,
    /// Delete action.
    ZIMRoomAttributesUpdateActionDelete = 1

};

/// Description: Room attributes update action.
typedef NS_ENUM(NSUInteger, ZIMRoomMemberAttributesUpdateAction) {
    /// Set action.
    ZIMRoomMemberAttributesUpdateActionSet = 0

};

/// the direction of the message.
///
/// Description: Describes whether the current message was sent or received.
typedef NS_ENUM(NSUInteger, ZIMMessageDirection) {
    /// Message has been sent.
    ZIMMessageDirectionSend = 0,
    /// Message accepted.
    ZIMMessageDirectionReceive = 1
};

/// The status of the message being sent.
///
/// Description: Describes the condition of the currently sent message.
typedef NS_ENUM(NSUInteger, ZIMMessageSentStatus) {
    /// The message is being sent.
    ZIMMessageSentStatusSending = 0,
    /// Message sent successfully.
    ZIMMessageSentStatusSendSuccess = 1,
    /// Message sending failed.
    ZIMMessageSentStatusSendFailed = 2

};

/// conversation type.
typedef NS_ENUM(NSUInteger, ZIMConversationType) {

    ZIMConversationTypePeer = 0,

    ZIMConversationTypeRoom = 1,

    ZIMConversationTypeGroup = 2

};

typedef NS_ENUM(NSUInteger, ZIMConversationEvent) {

    ZIMConversationEventAdded = 0,

    ZIMConversationEventUpdated = 1,

    ZIMConversationEventDisabled = 2

};

typedef NS_ENUM(NSUInteger, ZIMConversationNotificationStatus) {

    ZIMConversationNotificationStatusNotify = 1,

    ZIMConversationNotificationStatusDoNotDisturb = 2

};

/// Description: Group events.
typedef NS_ENUM(NSUInteger, ZIMGroupEvent) {
    /// Create groups.
    ZIMGroupEventCreated = 1,
    /// Disband the group.
    ZIMGroupEventDismissed = 2,
    /// Join the group.
    ZIMGroupEventJoined = 3,
    /// Invite to the group.
    ZIMGroupEventInvited = 4,
    /// Leave the group.
    ZIMGroupEventLeft = 5,
    /// Kick out of the group.
    ZIMGroupEventKickedout = 6

};

typedef NS_ENUM(NSUInteger, ZIMGroupState) {
    /// Quit
    ZIMGroupStateQuit = 0,
    /// Enter
    ZIMGroupStateEnter = 1

};

typedef NS_ENUM(NSUInteger, ZIMGroupMemberEvent) {

    ZIMGroupMemberEventJoined = 1,

    ZIMGroupMemberEventLeft = 2,

    ZIMGroupMemberEventKickedout = 4,

    ZIMGroupMemberEventInvited = 5

};

typedef NS_ENUM(NSUInteger, ZIMGroupMemberState) {

    ZIMGroupMemberStateQuit = 0,

    ZIMGroupMemberStateEnter = 1

};

typedef NS_ENUM(NSUInteger, ZIMGroupAttributesUpdateAction) {

    ZIMGroupAttributesUpdateActionSet = 0,

    ZIMGroupAttributesUpdateActionDelete = 1

};

typedef NS_ENUM(NSUInteger, ZIMGroupMessageNotificationStatus) {

    ZIMGroupMessageNotificationStatusNotify = 1,

    ZIMGroupMessageNotificationStatusDoNotDisturb = 2

};

typedef int ZIMGroupMemberRole;
#define ZIMGroupMemberRoleOwner 1
#define ZIMGroupMemberRoleMember 3

// MARK: - Model

/// Error infomation
///
/// Description: Error infomation.
///
@interface ZIMError : NSObject

/// The storage path of the log files. Refer to the official website document for the default path.
@property (nonatomic, assign) ZIMErrorCode code;

/// Error infomation description.
@property (nonatomic, copy) NSString *message;

@end

/// User information object.
///
/// Description: Identifies a unique user.
///
/// Caution: Note that the userID must be unique under the same appID, otherwise mutual kicks out will occur.
/// It is strongly recommended that userID corresponds to the user ID of the business APP,
/// that is, a userID and a real user are fixed and unique, and should not be passed to the SDK in a random userID.
/// Because the unique and fixed userID allows ZEGO technicians to quickly locate online problems.
///
@interface ZIMUserInfo : NSObject

/// User ID, a string with a maximum length of 32 bytes or less. Only support numbers, English characters and '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '=', '-', '`', ';', '’', ',', '.', '<', '>', '/', '\'.
@property (nonatomic, copy) NSString *userID;

/// User name, a string with a maximum length of 64 bytes or less
@property (nonatomic, copy) NSString *userName;

@end

@interface ZIMUserFullInfo : NSObject

/// User base info. including userID and userName.
@property (nonatomic, strong) ZIMUserInfo *baseInfo;

@property (nonatomic, copy) NSString *userAvatarUrl;
/// User extended data.
@property (nonatomic, copy) NSString *extendedData;

@end

@interface ZIMUsersInfoQueryConfig : NSObject

/// User base info. including userID and userName.
@property (nonatomic, assign) BOOL isQueryFromServer;

@end

// MARK: - Model

@interface ZIMAppConfig : NSObject

@property (nonatomic, assign) unsigned int appID;

@property (nonatomic, copy) NSString *appSign;

@end

/// Log configuration
///
/// Description: Configure the storage path of log files and the maximum log file size.
///
@interface ZIMLogConfig : NSObject

/// The storage path of the log files. Refer to the official website document for the default path.
@property (nonatomic, copy) NSString *logPath;

/// The maximum log file size (Bytes). The default maximum size is 5MB (5 * 1024 * 1024 Bytes)
@property (nonatomic, assign) unsigned long long logSize;

@end

@interface ZIMCacheConfig : NSObject

@property (nonatomic, copy) NSString *cachePath;

@end

/// Description:Offline push configuration.
@interface ZIMPushConfig : NSObject

/// Description: Used to set the push title.
@property (nonatomic, copy) NSString *title;

/// Description: Used to set offline push content.
@property (nonatomic, copy) NSString *content;

/// Description: This parameter is used to set the pass-through field of offline push.
@property (nonatomic, copy) NSString *payload;

@property (nonatomic, copy) NSString *resourcesID;

@end

/// Base class of message object
///
/// Description: Identifies the basic parameters of a message.
///
/// Caution: Some of the parameters, such as Message ID, only have values ​​during the callback.
/// Developers do not need to assign values ​​to these parameters when they actively create this object for sending messages.
///
@interface ZIMMessage : NSObject

/// Identifies the type of this message.
@property (nonatomic, assign, readonly) ZIMMessageType type;

/// Description: The unique ID that identifies this message.
///
/// Use cases: Can be used to index other messages.
///
/// Caution: When the developer actively creates a message, there is no need to modify this parameter.
/// This parameter only has a value during callback.
@property (nonatomic, assign, readonly) long long messageID;

@property (nonatomic, assign, readonly) long long localMessageID;

/// Description: Identifies which User sent this message.
///
/// Use cases: Used to show who sent a message.
///
/// Caution: When the developer actively creates a message, there is no need to modify this parameter.
/// This parameter only has a value during callback.
@property (nonatomic, copy, readonly) NSString *senderUserID;

/// Description: Session ID. Ids of the same session type are unique.
@property (nonatomic, copy, readonly) NSString *conversationID;

/// Description: Used to describe whether a message is sent or received.
@property (nonatomic, assign, readonly) ZIMMessageDirection direction;

/// Description: Describes the sending status of a message.
@property (nonatomic, assign, readonly) ZIMMessageSentStatus sentStatus;

/// Description: The type of session to which the message belongs.
@property (nonatomic, assign, readonly) ZIMConversationType conversationType;

/// Description: Identifies the sending time of a message
///
/// Use cases: Used to present the sending time of a message, and can be used for message sorting.
///
/// Caution: This is a standard UNIX timestamp, in milliseconds.
@property (nonatomic, assign, readonly) unsigned long long timestamp;

/// Description: Indicates the sequence number of the message in the session.
@property (nonatomic, assign, readonly) long long conversationSeq;

/// Description:The larger the orderKey, the newer the message, and can be used for ordering messages.
@property (nonatomic, assign, readonly) long long orderKey;

@property (nonatomic, assign, readonly) BOOL isUserInserted;

@property (nonatomic, assign, readonly) ZIMMessageReceiptStatus receiptStatus;

@property (nonatomic, copy) NSString *extendedData;
@end

/// Base class for media message objects.
///
/// Detail description: Identifies a media message.
///
/// Note: This base class is the basis of all media messages and contains the properties required by media messages.
@interface ZIMMediaMessage : ZIMMessage

/// Detail description: The local path of the media message.
///
/// Required: Required by the sender, otherwise the message will fail to be sent.
@property (nonatomic, copy) NSString *fileLocalPath;

/// Detail description: The external download url of the media message is used for the developer to transparently transmit the media file to other users by filling in this URL when the developer uploads the media file to his own server.
///
/// Required or not: optional at the sender.
@property (nonatomic, copy) NSString *fileDownloadUrl;

/// Detail description: The unique ID of the media file.
///
/// Required or not: The sender does not need to fill in, this value is generated by the SDK.
@property (nonatomic, copy, readonly) NSString *fileUID;

/// Detail description: The filename of the media file.
///
/// Required or not: The sender does not need to fill in, this value is generated by the SDK.
@property (nonatomic, copy, readonly) NSString *fileName;

/// Detail description: The size of the media file.
///
/// Required or not: The sender does not need to fill in, this value is generated by the SDK.
@property (nonatomic, assign, readonly) long long fileSize;

@end

/// Normal text message object.
///
/// Description: Identifies the basic parameters of a message.
///
/// Caution: If the Type parameter of the base class is Text during callback,
/// you can force the base class message object to be of this type.
///
@interface ZIMTextMessage : ZIMMessage

/// The content of the text message.
@property (nonatomic, copy) NSString *message;

- (instancetype)initWithMessage:(NSString *)message;

@end

@interface ZIMSystemMessage : ZIMMessage

/// The content of the system message.
@property (nonatomic, copy) NSString *message;

- (instancetype)initWithMessage:(NSString *)message;

@end

/// Custom binary message object.
///
/// Description: Identifies a binary message.
///
/// Caution: If the Type parameter of the base class is Custom during callback,
/// you can force the base class message object to be of this type.
///
@interface ZIMCommandMessage : ZIMMessage

/// The content of the command message.
@property (nonatomic, copy) NSData *message;

- (instancetype)initWithMessage:(NSData *)message;

@end

/// Description: The barrage message class does not appear in the session and does not store historical messages.
@interface ZIMBarrageMessage : ZIMMessage

/// The content of the barrage message.
@property (nonatomic, copy) NSString *message;

- (instancetype)initWithMessage:(NSString *)message;

@end

/// Description: Identifies the basic parameters of a message.
///
/// Caution: If the Type parameter of the base class is Image during callback, you can force the base class message object to be of this type.
@interface ZIMImageMessage : ZIMMediaMessage

/// Detail description: Thumbnail external download URL of the image file. When developers upload thumbnails to their own servers, the SDK can pass through this field to other users.
///
/// Required or not: optional on the sender side, this field will only take effect when fileDownloadUrl is filled in.
@property (nonatomic, copy) NSString *thumbnailDownloadUrl;

/// Detailed description: The thumbnail local path of the image file.
///
/// Required or not: The sender does not need to fill in it. After calling [downloadMediaFile] to download, the SDK will generate this value.
@property (nonatomic, copy, readonly) NSString *thumbnailLocalPath;

/// Detail description: Large image external download URL of the image file. When developers upload large image to their own servers, the SDK can pass through this field to other users.
///
/// Required or not: optional on the sender side, this field will only take effect when fileDownloadUrl is filled in.
@property (nonatomic, copy) NSString *largeImageDownloadUrl;

/// Detailed description: The large image local path of the image file.
///
/// Required or not: The sender does not need to fill in it. After calling [downloadMediaFile] to download, the SDK will generate this value.
@property (nonatomic, copy, readonly) NSString *largeImageLocalPath;

@property (nonatomic, assign, readonly) CGSize originalImageSize;

@property (nonatomic, assign, readonly) CGSize largeImageSize;

@property (nonatomic, assign, readonly) CGSize thumbnailSize;

- (instancetype)initWithFileLocalPath:(NSString *)fileLocalPath;

@end

/// Description: Identifies the basic parameters of a message.
///
/// Caution: If the Type parameter of the base class is File during callback,
/// you can force the base class message object to be of this type.
@interface ZIMFileMessage : ZIMMediaMessage

- (instancetype)initWithFileLocalPath:(NSString *)fileLocalPath;

@end

/// Description: Identifies the basic parameters of a message.
///
/// Caution: If the Type parameter of the base class is Audio during callback,
/// you can force the base class message object to be of this type.
@interface ZIMAudioMessage : ZIMMediaMessage

/// Detail description: The duration of the audio file.
///
/// Required: Required by the sender, if not filled, the audio message will fail to be sent.
@property (nonatomic, assign) unsigned int audioDuration;

- (instancetype)initWithFileLocalPath:(NSString *)fileLocalPath
                        audioDuration:(unsigned int)audioDuration;

@end

@interface ZIMRevokeMessage : ZIMMessage

@property (nonatomic, assign, readonly) ZIMRevokeType revokeType;

@property (nonatomic, assign, readonly) unsigned long long revokeTimestamp;

@property (nonatomic, copy, readonly) NSString *operatedUserID;

@property (nonatomic, assign, readonly) ZIMMessageType originalMessageType;

@property (nonatomic, copy, readonly) NSString *originalTextMessageContent;

@property (nonatomic, copy, readonly) NSString *revokeExtendedData;

@property (nonatomic, assign, readonly) ZIMMessageRevokeStatus revokeStatus;

@end

/// Description: Identifies the basic parameters of a message.
///
/// Caution: If the Type parameter of the base class is Video during callback,
/// you can force the base class message object to be of this type.
@interface ZIMVideoMessage : ZIMMediaMessage

/// Detail description: The duration of the video file.
///
/// Required: Required by the sender, if not filled, the video message will fail to be sent.
@property (nonatomic, assign) unsigned int videoDuration;

/// Detail description: Video first frame external download URL of the video file. When developers upload thumbnails to their own servers, the SDK can pass through this field to other users.
///
/// Required or not: optional on the sender side, this field will only take effect when fileDownloadUrl is filled in.
@property (nonatomic, copy) NSString *videoFirstFrameDownloadUrl;

/// Detailed description: The video first frame local path of the video file.
///
/// Required or not: The sender does not need to fill in it. After calling [downloadMediaFile] to download, the SDK will generate this value.
@property (nonatomic, copy, readonly) NSString *videoFirstFrameLocalPath;

@property (nonatomic, assign, readonly) CGSize videoFirstFrameSize;

- (instancetype)initWithFileLocalPath:(NSString *)fileLocalPath
                        videoDuration:(unsigned int)videoDuration;

@end

/// Details: Configure message sending.
@interface ZIMMessageSendConfig : NSObject

/// Description: Configures the offline push function.
@property (nonatomic, strong, nullable) ZIMPushConfig *pushConfig;

/// Enumeration value used to set message priority. The default value is Low.
@property (nonatomic, assign) ZIMMessagePriority priority;

@property (nonatomic, assign) BOOL hasReceipt;

@end

/// Example Query message configuration.
@interface ZIMMessageQueryConfig : NSObject

/// Description: Query the anchor point of the message.
/// Required: This parameter is not required for the first query but is required for subsequent paging queries.
/// Default value: The default value is nil.
@property (nonatomic, strong, nullable) ZIMMessage *nextMessage;

/// Description: Number of query messages. The default value is 0.
@property (nonatomic, assign) unsigned int count;

/// Description: Indicates whether the query is in reverse order. The default value is NO.
@property (nonatomic, assign) BOOL reverse;

@end

/// Description: Revoke configurations related to messages.
@interface ZIMMessageRevokeConfig : NSObject

/// Description: Configures the offline push function, If Android or iOS platform is integrated, it is strongly recommended to configure this.
/// Required: Not mandatory.
@property (nonatomic, strong, nullable) ZIMPushConfig *pushConfig;

/// Description: revoking additional messages.
@property (nonatomic, copy) NSString *revokeExtendedData;

@end

/// Delete message configuration.
@interface ZIMMessageDeleteConfig : NSObject

/// Description: Whether to remove flags for server messages. The default value is YES.
@property (nonatomic, assign) BOOL isAlsoDeleteServerMessage;

@end

typedef void (^ZIMMessageAttachedCallback)(ZIMMessage *message);

typedef void (^ZIMMediaUploadingProgress)(ZIMMediaMessage *message,
                                          unsigned long long currentFileSize,
                                          unsigned long long totalFileSize);

@interface ZIMMessageSendNotification : NSObject

@property (nonatomic, copy, nullable) ZIMMessageAttachedCallback onMessageAttached;

@end

@interface ZIMMediaMessageSendNotification : NSObject

@property (nonatomic, copy, nullable) ZIMMessageAttachedCallback onMessageAttached;
@property (nonatomic, copy, nullable) ZIMMediaUploadingProgress onMediaUploadingProgress;

@end

/// Detailed description: receipt information.
@interface ZIMMessageReceiptInfo : NSObject

/// Detail description: receipt status.
@property (nonatomic, assign) ZIMMessageReceiptStatus status;

/// Detailed Description: Message ID.
///
/// Business scenario: Developers can match the loaded message list according to this ID.
///
/// Is it required: No, SDK fills in.
@property (nonatomic, assign) long long messageID;

/// Detailed Description: Session ID.
///
/// Business scenario: Receipt information used to indicate which session this belongs to.
///
/// Is it required: No, SDK fills in.
@property (nonatomic, copy) NSString *conversationID;

/// Detailed Description: Session type.
///
/// Business scenario: Indicates the type of the session.
///
/// Is it required: If no, the SDK is filled.
@property (nonatomic, assign) ZIMConversationType conversationType;

/// Detailed description: Indicates the number of people who have read the receipt.
///
/// Business scenario: When used to query receipt information, it can display how many people have read the message.
///
/// Is it required: no.
///
/// Default: 0.
///
/// Note: This value indicates how many people have read the message, it is only applicable to the message has been read; if the message is not sent by yourself, the value is 0.
@property (nonatomic, assign) unsigned int readMemberCount;

/// Detailed description: Indicates the number of unread people of the receipt.
///
/// Business scenario: When used to query receipt information, it can display how many people have unread the message.
///
/// Is it required: no.
///
/// Default: 0.
///
/// Note: This value indicates how many people have not read the message, and it is only applicable to the message that has been read; if the message is not sent by yourself, the value is 0.
@property (nonatomic, assign) unsigned int unreadMemberCount;

@end

@interface ZIMConversation : NSObject

/// Description: conversationID.
@property (nonatomic, copy) NSString *conversationID;

/// Description: conversationName is the same as the groupName/userName value corresponding to the session.
@property (nonatomic, copy) NSString *conversationName;

@property (nonatomic, copy) NSString *conversationAvatarUrl;

/// Description: conversation type.
@property (nonatomic, assign) ZIMConversationType type;

/// Description: session notification status.
@property (nonatomic, assign) ZIMConversationNotificationStatus notificationStatus;

/// Description: Session unread.
@property (nonatomic, assign) unsigned int unreadMessageCount;

/// Description: last message.
@property (nonatomic, strong) ZIMMessage *lastMessage;

/// Description: OrderKey is used to describe the order of messages in the session. The larger orderKey is, the newer it is.
@property (nonatomic, assign) long long orderKey;

@end

/// Description: Query the relevant configuration of the session.
@interface ZIMConversationQueryConfig : NSObject

/// Description: Session that needs to be queried.
@property (nonatomic, strong, nullable) ZIMConversation *nextConversation;

/// Description: The number of sessions to query.
@property (nonatomic, assign) unsigned int count;

@end

/// Description: Delete session configuration.
@interface ZIMConversationDeleteConfig : NSObject

/// Description: just remove the session flag.
@property (nonatomic, assign) BOOL isAlsoDeleteServerConversation;

@end

/// Description: Session change information.
@interface ZIMConversationChangeInfo : NSObject

/// Description: session events.
@property (nonatomic, assign) ZIMConversationEvent event;

/// Description: Session specific information.
@property (nonatomic, strong) ZIMConversation *conversation;

@end

/// Description: Session sent status change information.
@interface ZIMMessageSentStatusChangeInfo : NSObject

/// Description: sentStatus
@property (nonatomic, assign) ZIMMessageSentStatus status;

/// Description: Session specific information.
@property (nonatomic, strong) ZIMMessage *message;

@end

/// Room information object.
///
/// Description: Identifies a unique room.
///
@interface ZIMRoomInfo : NSObject

/// Room ID, a string with a maximum length of 32 bytes or less. Only support numbers, English characters and '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '=', '-', '`', ';', '’', ',', '.', '<', '>', '/', '\'.
@property (nonatomic, copy) NSString *roomID;

/// Room name, a string with a maximum length of 64 bytes or less
@property (nonatomic, copy) NSString *roomName;

@end

/// The room details object.
///
/// Description: Identifies the basic parameters of a message.
///
/// Caution: Identifies the detailed information of a room.
///
@interface ZIMRoomFullInfo : NSObject

/// The base object of the room.
@property (nonatomic, strong) ZIMRoomInfo *baseInfo;

@end

/// Configuration for querying member.
///
/// Description: When querying member, you need to configure this object.
///
@interface ZIMRoomMemberQueryConfig : NSObject

/// Description: The flag of the paging query. For the first query, set this field to an empty string. If the "nextFlag" field of the callback is not an empty string, it needs to be set here to continue the query on the next page.
@property (nonatomic, copy) NSString *nextFlag;

/// Description: How many messages are retrieved in one query.
///
/// Caution: To obtain messages in pages to reduce overhead, it is recommended to obtain within 100 messages at a time.
@property (nonatomic, assign) unsigned int count;

@end

///
///
///
///
/// Room advanced config.
@interface ZIMRoomAdvancedConfig : NSObject

/// Description: Room attributes of a room.
@property (nonatomic, strong, nullable) NSDictionary<NSString *, NSString *> *roomAttributes;

@property (nonatomic, assign) unsigned int roomDestroyDelayTime;

@end

/// The behavior attribute set by the room attribute.
@interface ZIMRoomAttributesSetConfig : NSObject

/// Description: Whether the operation is mandatory, that is, the property of the room whose owner is another user can be modified.
@property (nonatomic, assign) BOOL isForce;

/// Description: Room attributes are automatically deleted after the owner leaves the room.
@property (nonatomic, assign) BOOL isDeleteAfterOwnerLeft;

/// Description: Whether to update the owner of the room attribute involved.
@property (nonatomic, assign) BOOL isUpdateOwner;

@end

/// The behavior attribute set by the room attribute.
@interface ZIMRoomAttributesBatchOperationConfig : NSObject

/// Description: Whether the operation is mandatory, that is, the property of the room whose owner is another user can be modified.
@property (nonatomic, assign) BOOL isForce;

/// Description: Room attributes are automatically deleted after the owner leaves the room.
@property (nonatomic, assign) BOOL isDeleteAfterOwnerLeft;

/// Description: Whether to update the owner of the room attribute involved.
@property (nonatomic, assign) BOOL isUpdateOwner;

@end
///
///
///
///
@interface ZIMRoomAttributesDeleteConfig : NSObject

/// Description: Whether the operation is mandatory, that is, the property of the room whose owner is another user can be deleted.
@property (nonatomic, assign) BOOL isForce;

@end

///
///
///
///Notice of Room Attribute Change.
@interface ZIMRoomAttributesUpdateInfo : NSObject

/// Description: Behavioral information of room attribute change notification.
@property (nonatomic, assign) ZIMRoomAttributesUpdateAction action;

/// Description:  Room attributes.
@property (nonatomic, strong) NSDictionary<NSString *, NSString *> *roomAttributes;

@end

///Room user attribute information.
@interface ZIMRoomMemberAttributesInfo : NSObject

/// Description: Detail description: User ID.
@property (nonatomic, copy) NSString *userID;
///
/// Description: room user attributes.
@property (nonatomic, strong) NSDictionary<NSString *, NSString *> *attributes;

@end

///Room user attribute operation information.
@interface ZIMRoomMemberAttributesOperatedInfo : NSObject

/// Description: room user attribute information.
@property (nonatomic, strong) ZIMRoomMemberAttributesInfo *attributesInfo;

/// Description: The key of the room user attribute operation failure.
@property (nonatomic, strong) NSArray<NSString *> *errorKeys;

@end

///Room user attribute update information.
@interface ZIMRoomMemberAttributesUpdateInfo : NSObject

/// Description: room user attributes.
@property (nonatomic, strong) ZIMRoomMemberAttributesInfo *attributesInfo;

@end

///Room user property settings configuration.
@interface ZIMRoomMemberAttributesSetConfig : NSObject

/// Description: Configure whether the room user attributes are saved after the user leaves the room.
@property (nonatomic, assign) BOOL isDeleteAfterOwnerLeft;

@end

///Room user attribute query configuration.
@interface ZIMRoomMemberAttributesQueryConfig : NSObject

/// Description: Query anchor for room user properties.
@property (nonatomic, copy) NSString *nextFlag;

/// Description: The number of paginated queries.
@property (nonatomic, assign) unsigned int count;

@end

///Room operation information.
@interface ZIMRoomOperatedInfo : NSObject

/// Description: UserID of the operator.
@property (nonatomic, copy) NSString *userID;

@end
///
///
///
///
/// When userInfo is queried, the failed userInfo is displayed through this data class.
@interface ZIMErrorUserInfo : NSObject

/// Description:userID.
@property (nonatomic, copy) NSString *userID;

/// Description: Description Reason for the query failure.
@property (nonatomic, assign) unsigned int reason;

@end

///
///
///
/// group information.
@interface ZIMGroupInfo : NSObject

/// Description: groupID.
@property (nonatomic, copy) NSString *groupID;

/// Description: Group name.
@property (nonatomic, copy) NSString *groupName;

@property (nonatomic, copy) NSString *groupAvatarUrl;

@end

///
///
///
/// Description: complete group information.
@interface ZIMGroupFullInfo : NSObject

/// Description: basic group information.
@property (nonatomic, strong) ZIMGroupInfo *baseInfo;

/// Description: basic group notice.
@property (nonatomic, copy) NSString *groupNotice;

/// Description: where developers can customize key-value
@property (nonatomic, strong) NSDictionary<NSString *, NSString *> *groupAttributes;

/// Description: group DND status.
@property (nonatomic, assign) ZIMGroupMessageNotificationStatus notificationStatus;

@end

/// Description:  group class.
@interface ZIMGroup : NSObject

/// Description: basic group information.
@property (nonatomic, strong) ZIMGroupInfo *baseInfo;

/// Description: group DND status.
@property (nonatomic, assign) ZIMGroupMessageNotificationStatus notificationStatus;

@end

///
///
///
/// Group member information.
@interface ZIMGroupMemberInfo : ZIMUserInfo

/// Description: Group nickname.
@property (nonatomic, copy) NSString *memberNickname;

/// Description: group role.
@property (nonatomic, assign) ZIMGroupMemberRole memberRole;

@property (nonatomic, copy) NSString *memberAvatarUrl;

@end

///
///
///
/// Information that the group has operated on.
@interface ZIMGroupOperatedInfo : NSObject
/// Description: Group member information.
@property (nonatomic, strong) ZIMGroupMemberInfo *operatedUserInfo;

@property (nonatomic, copy) NSString *userID;

@property (nonatomic, copy) NSString *userName;

@property (nonatomic, copy) NSString *memberNickname;

@property (nonatomic, assign) ZIMGroupMemberRole memberRole;

@end

///
///
///
/// group member query configuration.
@interface ZIMGroupMemberQueryConfig : NSObject

/// Description: nextFlag.
@property (nonatomic, assign) unsigned int nextFlag;

/// Description: count.
@property (nonatomic, assign) unsigned int count;

@end

///
///
///
/// Group advanced configuration.
@interface ZIMGroupAdvancedConfig : NSObject

@property (nonatomic, copy) NSString *groupNotice;

@property (nonatomic, strong, nullable) NSDictionary<NSString *, NSString *> *groupAttributes;

@end

///
///
///
/// Group attribute update information.
@interface ZIMGroupAttributesUpdateInfo : NSObject

/// Description: Group attribute update action.
@property (nonatomic, assign) ZIMGroupAttributesUpdateAction action;

/// Description: group properties.
@property (nonatomic, strong) NSDictionary<NSString *, NSString *> *groupAttributes;

@end

@interface ZIMGroupMessageReceiptMemberQueryConfig : NSObject

@property (nonatomic, assign) unsigned int nextFlag;

@property (nonatomic, assign) unsigned int count;

@end

typedef NS_ENUM(NSUInteger, ZIMCallUserState) {
    ZIMCallUserStateInviting = 0,
    ZIMCallUserStateAccepted = 1,
    ZIMCallUserStateRejected = 2,
    ZIMCallUserStateCancelled = 3,
    ZIMCallUserStateOffline = 4,
    ZIMCallUserStateReceived = 5
};

/// Call invitation user information.
@interface ZIMCallUserInfo : NSObject

/// Description:  userID.
@property (nonatomic, copy) NSString *userID;

/// Description:  user status.
@property (nonatomic, assign) ZIMCallUserState state;

@end

/// The behavior property of the Send Call Invitation setting.
@interface ZIMCallInviteConfig : NSObject

/// Description: The timeout setting of the call invitation, the unit is seconds. The default value is 90s.
@property (nonatomic, assign) unsigned int timeout;

/// Description: Extended field, through which the inviter can carry information to the invitee.
@property (nonatomic, copy) NSString *extendedData;

/// Description: Configures the offline push function.
@property (nonatomic, strong, nullable) ZIMPushConfig *pushConfig;

@end
/// Behavior property that cancels the call invitation setting.
@interface ZIMCallCancelConfig : NSObject

/// Description: Extended field.
@property (nonatomic, copy) NSString *extendedData;

@end

/// Behavior property that accept the call invitation setting.
@interface ZIMCallAcceptConfig : NSObject

/// Description: Extended field.
@property (nonatomic, copy) NSString *extendedData;

@end

/// The behavior property of the reject call invitation setting.
@interface ZIMCallRejectConfig : NSObject

/// Description: Extended field, through which the inviter can carry information to the invitee.
@property (nonatomic, copy) NSString *extendedData;

@end

/// Call invitation sent message.
@interface ZIMCallInvitationSentInfo : NSObject

/// Description: The timeout setting of the call invitation, the unit is seconds.
@property (nonatomic, assign) unsigned int timeout;

/// Description: User id that has not received a call invitation.
@property (nonatomic, strong) NSArray<ZIMCallUserInfo *> *errorInvitees;

@end

/// Information to accept the call invitation.
@interface ZIMCallInvitationReceivedInfo : NSObject

/// Description: The timeout setting of the call invitation, the unit is seconds.
@property (nonatomic, assign) unsigned int timeout;

/// Description: Inviter ID.
@property (nonatomic, copy) NSString *inviter;

/// Description: Extended field, through which the inviter can carry information to the invitee.
@property (nonatomic, copy) NSString *extendedData;

@end

/// Cancel the call invitation message.
@interface ZIMCallInvitationCancelledInfo : NSObject

/// Description:  The inviter ID of the call invitation.
@property (nonatomic, copy) NSString *inviter;

/// Description: Extended field, through which the inviter can carry information to the invitee.
@property (nonatomic, copy) NSString *extendedData;

@end

/// Accept the call invitation message.
@interface ZIMCallInvitationAcceptedInfo : NSObject

/// Description: Invitee ID.
@property (nonatomic, copy) NSString *invitee;

/// Description: Extended field, through which the inviter can carry information to the invitee.
@property (nonatomic, copy) NSString *extendedData;

@end

/// Reject the call invitation message.
@interface ZIMCallInvitationRejectedInfo : NSObject

/// Description: Invitee ID.
@property (nonatomic, copy) NSString *invitee;

/// Description: Extended field, through which the inviter can carry information to the invitee.
@property (nonatomic, copy) NSString *extendedData;

@end

@interface ZIMCallInvitationTimeoutInfo : NSObject

@property (nonatomic, copy) NSString *inviter;

@end

/// Supported version: 1.2.0 and above.
///
/// Detailed description: After the developer uploads the log, execute the callback of the result.
///
/// Use cases: Developers can use the [errorCode] in the callback to determine whether the upload is successful.
///
/// Notification timing: When the developer calls the [uploadLog] interface, the callback will be triggered.
///
/// Related interface: call log upload via [uploadLog], success or failure will be notified through this callback.
typedef void (^ZIMLogUploadedCallback)(ZIMError *errorInfo);

/// @param errorInfo Error code.
typedef void (^ZIMLoggedInCallback)(ZIMError *errorInfo);

// MARK: Main

/// Callback of the result of renewing the token.
///
/// @param token The renewed token.
/// @param errorInfo Error code.
typedef void (^ZIMTokenRenewedCallback)(NSString *token, ZIMError *errorInfo);

typedef void (^ZIMUserNameUpdatedCallback)(NSString *userName, ZIMError *errorInfo);

typedef void (^ZIMUserAvatarUrlUpdatedCallback)(NSString *userAvatarUrl, ZIMError *errorInfo);

typedef void (^ZIMUserExtendedDataUpdatedCallback)(NSString *extendedData, ZIMError *errorInfo);

/// Supported version: 2.0.0 and above.
///
/// Detailed description: Callback after developer queries user information.
///
/// Use cases: The developer can check whether the login succeeded by using [errorCode] in this callback.
///
/// Notification timing: This callback is triggered when a developer invokes the [queryUserInfo] interface.
///
/// Related interface: Run the queryUserInfo command to query information.
///
/// @param errorInfo  Error code.
/// @param userList  List of the userInfo queried.
/// @param errorUserList Failed to query the userInfo list.
typedef void (^ZIMUsersInfoQueriedCallback)(NSArray<ZIMUserFullInfo *> *userList,
                                            NSArray<ZIMErrorUserInfo *> *errorUserList,
                                            ZIMError *errorInfo);

// MARK: Conversation

/// Available since: 2.0.0 and above.
///
/// Description: After the session list is queried, the callback is used to return the query result.
///
/// Use cases: The logic after the session list query can be done in this callback.
///
/// When to call /Trigger: Description Triggered when the session list is queried.
///
/// Related APIs: [queryConversationList].
///
/// @param conversationList Session list.
/// @param errorInfo Error code for querying the conversation list. 0 means querying the session list succeeds, non-0 means querying the session list fails. If there is a failure to query the call list, the developer should check the official website error code document to query the solution.
typedef void (^ZIMConversationListQueriedCallback)(NSArray<ZIMConversation *> *conversationList,
                                                   ZIMError *errorInfo);

/// Available since: 2.0.0 and above.
///
/// Description: After a session is deleted, the deletion result is returned using this callback.
///
/// Use cases: You can do the deleted session logic in this callback.
///
/// When to call /Trigger: Description Triggered after the session was deleted.
///
/// Related APIs: [[deleteConversation].
///
/// @param errorInfo Delete the error code of the call. 0 means that the session was deleted successfully, non-0 means that the session was deleted failed. If the deletion of the call fails, the developer should check the official website error code document to query the solution.
/// @param conversationID Conversation ID.
/// @param conversationType Conversation type.
typedef void (^ZIMConversationDeletedCallback)(NSString *conversationID,
                                               ZIMConversationType conversationType,
                                               ZIMError *errorInfo);

/// Available since: 2.0.0 and above.
///
/// Description: This callback returns the result of clearing a session if the session is not read.
///
/// Use cases: You can do clear unread logic in this callback.
///
/// When to call /Trigger: Triggered after clearing session unread.
///
/// Related APIs: [clearConversationUnreadMessageCount].
///
/// @param errorInfo Clears the error code for the number of unread calls. 0 means clearing the unread number of the session successfully, non-0 means the clearing the unread number of the session failed. If there is a failure to clear the number of unread calls, the developer should check the official website error code document to query the solution.
/// @param conversationID  Conversation ID.
/// @param conversationType Conversation type.
typedef void (^ZIMConversationUnreadMessageCountClearedCallback)(
    NSString *conversationID, ZIMConversationType conversationType, ZIMError *errorInfo);

/// Available since: 2.0.0 and above.
///
/// Description: This callback returns the result of group notification after the group notification status is set.
///
/// Use cases: You can do the logic after setting the group notification status in this callback.
///
/// When to call /Trigger: Triggered when the group notification status is set.
///
/// Related APIs: [setConversationNotificationStatus].
///
/// @param conversationID conversationID.
/// @param errorInfo Set the error code for the callback notification state. 0 means setting the call back notification state successfully, non-0 means setting the call back notification state fails. If there is a failure to set the callback notification status, the developer should check the official website error code document to query the solution.
/// @param conversationType Conversation type.
typedef void (^ZIMConversationNotificationStatusSetCallback)(NSString *conversationID,
                                                             ZIMConversationType conversationType,
                                                             ZIMError *errorInfo);

/// Available since: 2.5.0 and above.
///
/// Description: Set the callback interface for the read receipt conversation.
///
/// Use cases: Developers can judge whether the sending is successful through [errorCode] in the callback.
///
/// Trigger: When the developer calls the [sendConversationMessageReceiptRead] interface, this callback will be triggered.
///
/// Related APIs: The success or failure of the conversation read result set by [sendConversationMessageReceiptRead] will be notified through this callback.
///
/// @param conversationID ConversationID.
/// @param conversationType Conversation Type.
/// @param errorInfo  Set the error code of the receipt conversation read. 0 indicates that the setting is successful, and non-zero indicates that the setting fails. If the setting fails, the developer should check the [official website error code document](https://doc-zh.zego.im/article/11605) for solutions.
typedef void (^ZIMConversationMessageReceiptReadSentCallback)(NSString *conversationID,
                                                              ZIMConversationType conversationType,
                                                              ZIMError *errorInfo);

/// Supported Versions: 2.4.0 and above.

/// Detail description: A callback for the result of inserting a local message.

/// Business scenario: The developer can use the [errorCode] in the callback to determine whether the insertion is successful.

/// Notification timing: This callback is triggered when the developer calls the [insertMessageToLocalDB] interface.

/// Related interfaces: Insert local messages through [insertMessageToLocalDB], and the success or failure will be notified through this callback.
/// @param Message object.
/// @param errorInfo Insert the error code for the local message. 0 means inserting the message successfully, non-0 means inserting the message failed. If there is a failure to insert the message, the developer should check the official website error code document to query the solution.
typedef void (^ZIMMessageInsertedCallback)(ZIMMessage *message, ZIMError *errorInfo);

/// Callback of the result of sending the message.
///
/// Available since: 1.1.0 or above.
///
/// Description: This callback is triggered when the developer calls the [sendPeerMessage] and [sendRoomMessage] interfaces. The developer can check whether the callback is sent successfully by [errorCode] in the callback.
///
/// @param message The sent message object, from which parameters such as messageID can be obtained. If the sending fails, the messageID parameter in the message will be an empty string.
/// @param errorInfo Error information.
typedef void (^ZIMMessageSentCallback)(ZIMMessage *message, ZIMError *errorInfo);

/// The developer uses this callback to get a list of queried messages, which can be used to display historical messages.
///
/// @param messageList The message list of the query result.
/// @param errorInfo Error information.
typedef void (^ZIMMessageQueriedCallback)(NSString *conversationID,
                                          ZIMConversationType conversationType,
                                          NSArray<ZIMMessage *> *messageList, ZIMError *errorInfo);

/// Supported versions: 2.0.0 and above.
///
/// Detail description: After the message is deleted, the result of message deletion is returned through this callback.
///
/// Business scenario: The developer can judge whether the deletion is successful through the [errorCode] in the callback.
///
/// Notification timing: Triggered after calling the delete message interface [deleteMessage].
///
/// Related interface: [deleteMessage].
///
/// @param conversationID Conversation ID.
/// @param conversationType Conversation Type.
/// @param errorInfo Delete the error code of the message. 0 means the deletion of the message succeeded, non-0 means the deletion of the message failed. If the deletion message fails, the developer should check the official website error code document to query the solution.
typedef void (^ZIMMessageDeletedCallback)(NSString *conversationID,
                                          ZIMConversationType conversationType,
                                          ZIMError *errorInfo);

/// Available sinces: 2.5.0 and above.
///
/// Detail description: Consequential recap of withdrawal news.
///
/// Use cases: Developer allowed through this process during response [errorCode] Successful delivery.
///
/// Trigger: For our developers [revokeMessage] When contacting, this revoke will be sent.
///
/// Related APIs: If the revoked message is the latest message of the session, the [conversationChanged] callback will be triggered, and if the message is unread, the [conversationTotalUnreadMessageCountUpdated] callback will be triggered.
/// @param message Message object.
/// @param errorInfo Retraction notice. 0 representative withdraw successfully, non 0 withdraw unsuccessful. When the withdrawal fails, the developer responds to the [official website](https://doc-zh.zego.im/article/11605) Review the solution.
typedef void (^ZIMMessageRevokedCallback)(ZIMMessage *message, ZIMError *errorInfo);

/// Available since: 2.5.0 and above.
///
/// Description: Set the callback interface for the read receipt message.
///
/// Use cases: Developers can judge whether the setting is successful through [errorCode] in the callback.
///
/// Trigger: When the developer calls the [sendMessageReceiptsRead] interface, this callback will be triggered.
///
/// Related APIs: The success or failure of the message read result set by [sendMessageReceiptsRead] will be notified through this callback.
/// @param conversationID  ConversationID.
/// @param conversationType Conversation Type.
/// @param errorMessageIDs Set the message ID corresponding to the message receipt failure.
/// @param errorInfo Set the error code of the receipt message read. 0 indicates that the setting is successful, and non-zero indicates that the setting fails. If the setting fails, the developer should check the [official website error code document](https://doc-zh.zego.im/article/11605) for solutions.
typedef void (^ZIMMessageReceiptsReadSentCallback)(NSString *conversationID,
                                                   ZIMConversationType conversationType,
                                                   NSArray<NSNumber *> *errorMessageIDs,
                                                   ZIMError *errorInfo);

/// Available since: 2.5.0 and above.
///
/// Description: Callback interface for querying receipt message information.
///
/// Use cases: Developers can judge whether the sending is successful through [errorCode] in the callback.
///
/// Trigger: When the developer calls the [queryMessageReceiptsInfo] interface, this callback will be triggered.
///
/// Related APIs: through [queryMessageReceiptsInfo], whether the result of querying the receipt information is successful or not will be notified through this callback.
/// @param infos Query receipt information.
/// @param errorMessageIDs Query the wrong message ID of the message receipt information.
/// @param errorInfo Query the error code of the message receipt. 0 means the query is successful, and non-zero means the query failed. If the query fails, the developer should check the [official website error code document](https://doc-zh.zego.im/article/11605) for solutions.
typedef void (^ZIMMessageReceiptsInfoQueriedCallback)(NSArray<ZIMMessageReceiptInfo *> *infos,
                                                      NSArray<NSString *> *errorMessageIDs,
                                                      ZIMError *errorInfo);

/// Supported versions: 2.1.0 and above
///
/// Detail description: The progress callback for sending media messages.
///
/// Business scenario: The developer can obtain the sending progress of the media message through this callback.
///
/// Notification timing: When the developer calls the [sendMediaMessage] interface, the callback will be triggered, and will be triggered multiple times during the sending process.
///
/// Related interface: Through [sendMediaMessage], the sending progress will be notified through this callback.
typedef void (^ZIMMediaUploadingProgress)(ZIMMediaMessage *message,
                                          unsigned long long currentFileSize,
                                          unsigned long long totalFileSize);

/// Supported versions: 2.1.0 and above.
///
/// Detail description: The progress callback for downloading media messages.
///
/// Business scenario: The developer can obtain the download progress of the media message through this callback.
///
/// Notification timing: When the developer calls the [downloadMediaFile] interface, this callback will be triggered, and will be triggered multiple times during the download process.
///
/// Related APIs: Through [downloadMediaFile], the download progress will be notified through this callback.
typedef void (^ZIMMediaDownloadedCallback)(ZIMMediaMessage *message, ZIMError *errorInfo);

/// Supported versions: 2.1.0 and above.
///
/// Detail description: The result callback of media message download.
///
/// Business scenario: The developer can use the [errorCode] in the callback to determine whether the download is successful.
///
/// Notification timing: This callback is triggered when the developer calls the [downloadMediaFile] interface.
///
/// Related APIs: Download media messages through [downloadMediaFile], and it will be notified through this callback whether it is successful or not.
typedef void (^ZIMMediaDownloadingProgress)(ZIMMediaMessage *message,
                                            unsigned long long currentFileSize,
                                            unsigned long long totalFileSize);

// MARK: Room

/// Callback of the result of creating the room.
///
/// Available since: 1.1.0 or above.
///
/// Description: The callback of the result of creating the room.
///
/// Related APIs: Create a room through [createRoom], and the result of the creation will be notified through this callback.
///
/// @param roomInfo Details of the room created. If the creation fails, the roomID parameter in roomInfo will be an empty string.
/// @param errorInfo Error information.
typedef void (^ZIMRoomCreatedCallback)(ZIMRoomFullInfo *roomInfo, ZIMError *errorInfo);

/// Callback of the result of joining the room.
///
/// Available since: 1.1.0 or above.
///
/// Description: The callback of the result of joining the room.
///
/// Related APIs: Join the room through [joinRoom], and the result of joining will be notified through this callback.
///
/// @param roomInfo Details of the room joined. If the join fails, the roomID parameter in roomInfo will be an empty string.
/// @param errorInfo Error information.
typedef void (^ZIMRoomJoinedCallback)(ZIMRoomFullInfo *roomInfo, ZIMError *errorInfo);

/// Callback of the result of entering the room.
///
/// Available since: 2.1.0 or above.
///
/// Description: The callback of the result of entering the room.
///
/// Related APIs: Join the room through [enterRoom], and the result of joining will be notified through this callback.
///
/// @param roomInfo Details of the room joined. If the join fails, the roomID parameter in roomInfo will be an empty string.
/// @param errorInfo Error information.
typedef void (^ZIMRoomEnteredCallback)(ZIMRoomFullInfo *roomInfo, ZIMError *errorInfo);

/// Callback of the result of leaving the room.
///
/// Available since: 1.1.0 or above.
///
/// Description: The callback of the result of leaving the room.
///
/// Related APIs: Leave the room through [leaveRoom], and the result of leaving will be notified through this callback.
///
/// @param errorInfo Error information.
typedef void (^ZIMRoomLeftCallback)(NSString *roomID, ZIMError *errorInfo);

/// Callback of the result of querying the room members list.
///
/// Available since: 1.1.0 or above.
///
/// Description: Callback for the result of querying the room member list.
///
/// Related APIs: Query the list of room members through [queryRoomMember], and the query result will be notified through this callback.
///
/// @param memberList List of members in the room.
/// @param nextFlag The flag of the paging query. If this field is an empty string, the query has been completed. Otherwise, you need to set this value to the "nextFlag" field of ZIMRoomMemberQueryConfig for the next page query.
/// @param errorInfo Error information.
typedef void (^ZIMRoomMemberQueriedCallback)(NSString *roomID, NSArray<ZIMUserInfo *> *memberList,
                                             NSString *nextFlag, ZIMError *errorInfo);

/// Callback of the result of querying the online members count in the room.
///
/// Available since:  1.1.0 or above.
///
/// Description: Callback of the result of querying the online members count in the room.
///
/// Related APIs: You can check the online number of people in the room through [queryRoomOnlineMemberCount].
///
/// @param count The number of online members of the room.
/// @param errorInfo Error information.
typedef void (^ZIMRoomOnlineMemberCountQueriedCallback)(NSString *roomID, unsigned int count,
                                                        ZIMError *errorInfo);

/// Callback of the result of the room attributes operation.
///
/// Available since: 1.3.0.
///
/// Description: The callback of the result of room attributes operation.
///
/// @param errorInfo Error information.
typedef void (^ZIMRoomAttributesOperatedCallback)(NSString *roomID, NSArray<NSString *> *errorKeys,
                                                  ZIMError *errorInfo);

/// Callback of the result of the room attributes operation.
///
/// Available since: 1.3.0.
///
/// Description: The callback of the result of room attributes operation.
///
/// @param errorInfo Error information.
typedef void (^ZIMRoomAttributesBatchOperatedCallback)(NSString *roomID, ZIMError *errorInfo);

/// Callback of the result of the room attributes quering.
///
/// Available since: 1.3.0.
///
/// Description: The callback of the result of room attributes operation.
///
/// @param roomAttributes Room attributes.
/// @param errorInfo Error information.
typedef void (^ZIMRoomAttributesQueriedCallback)(
    NSString *roomID, NSDictionary<NSString *, NSString *> *roomAttributes, ZIMError *errorInfo);

/// Supported version: 2.4.0.
///
/// Detail description: Returns the result of the room user attribute operation.
///
/// Business scenario: After the custom attribute operation is performed, the success or failure can be known through this callback.
///
/// Notification timing: The result is returned after the room user attribute operation is completed.
///
/// Related interface: [setRoomMembersAttributes], add or modify room user attributes.
/// @param roomID Room ID.
/// @param infos The attributes information of the room member after the operation.
/// @param errorUserList List of UserIDs with errors.
/// @param errorInfo Error code for room user attribute operation. 0 means the room user attribute operation is successful, non-0 means the room user attribute operation fails. If the room user attribute operation fails, the developer should check the official website error code document to query the solution.
typedef void (^ZIMRoomMembersAttributesOperatedCallback)(
    NSString *roomID, NSArray<ZIMRoomMemberAttributesOperatedInfo *> *infos,
    NSArray<NSString *> *errorUserList, ZIMError *errorInfo);

/// Supported version: 2.4.0.
///
/// Detailed description: According to the UserID list, batch query results of room user attributes are returned.
///
/// Business scenario: After querying room user attributes, the success or failure and query results can be known through this callback.
///
/// Notification timing: The result will be returned after the room user attribute query is completed.
///
/// Related interface: [queryRoomMembersAttributes], query room user attributes.
/// @param roomID Room ID.
/// @param infos List of room user attributes.
/// @param errorInfo Error code for room user attribute operation. 0 means the room user attribute operation is successful, non-0 means the room user attribute operation fails. If the room user attribute operation fails, the developer should check the official website error code document to query the solution.
typedef void (^ZIMRoomMembersAttributesQueriedCallback)(
    NSString *roomID, NSArray<ZIMRoomMemberAttributesInfo *> *infos, ZIMError *errorInfo);

/// Supported version: 2.4.0.
///
/// Detail description: Returns the result of paging query of all user attribute lists in the room.
///
/// Business scenario: After querying room user attributes, the success or failure and query results can be known through this callback.
///
/// Notification timing: The result will be returned after the room user attribute query is completed.
///
/// Related interface: [queryRoomMemberAttributesList], query room user attributes.
/// @param roomID Room ID.
/// @param infos List of room user attributes.
/// @param The anchor of the next paging query. If it is empty, it means that the query has been completed.
/// @param errorInfo Error code for room user attribute operation. 0 means the room user attribute operation is successful, non-0 means the room user attribute operation fails. If the room user attribute operation fails, the developer should check the official website error code document to query the solution.
typedef void (^ZIMRoomMemberAttributesListQueriedCallback)(
    NSString *roomID, NSArray<ZIMRoomMemberAttributesInfo *> *infos, NSString *nextFlag,
    ZIMError *errorInfo);
/// Description: Returns the result of the group creation operation.
///
/// Use cases: After a group creation operation is performed, the success or failure can be determined by this callback.
///
/// When to call /Trigger: The result is returned after the group creation operation is complete.
///
/// Related API: [createGroup] : creates a group.
///
/// @param groupInfo groupInfo.
/// @param userList user list.
/// @param errorUserList errorUserList.
/// @param errorInfo Error code for group creation. 0 means the group creation is successful, non-0 means the group creation fails. If there is a failure to create a group, the developer should check the official website error code document to query the solution.
typedef void (^ZIMGroupCreatedCallback)(ZIMGroupFullInfo *groupInfo,
                                        NSArray<ZIMGroupMemberInfo *> *userList,
                                        NSArray<ZIMErrorUserInfo *> *errorUserList,
                                        ZIMError *errorInfo);

/// Description: Returns the result of the group dismiss operation.
///
/// Use cases: After a group disband operation is performed, the success of the operation can be determined by the callback.
///
/// When to call /Trigger: The result of the group disband operation is returned.
///
/// Related API: [createGroup],creates a group. [dismissGroup],dismissGroup.
///
/// @param errorInfo The error code for disbanding the group. 0 means that the group is successfully disbanded, and non-0 means that the group failed to be disbanded. If there is a failure to dissolve the group, the developer should check the official website error code document to find the solution.
/// @param groupID  Group ID.
typedef void (^ZIMGroupDismissedCallback)(NSString *groupID, ZIMError *errorInfo);

/// Description: Returns the result of the group join operation.
///
/// Use cases: After a group join operation is performed, the success or failure can be determined by this callback.
///
/// When to call /Trigger: The result of the group join operation is returned.
///
/// Related API:[joinGroup] : joins a group. [leaveGroup], leave the group.
typedef void (^ZIMGroupJoinedCallback)(ZIMGroupFullInfo *groupInfo, ZIMError *errorInfo);

/// Description: Returns the result of the group departure operation.
///
/// Use cases: After a group exit operation is performed, the success or failure can be determined by the callback.
///
/// When to call /Trigger: The result of the group departure operation is returned.
///
/// Related API:[leaveGroup], leave the group. [joinGroup], enter the group.
typedef void (^ZIMGroupLeftCallback)(NSString *groupID, ZIMError *errorInfo);

/// Description: Returns the result of inviting the user to join the group.
///
/// Use cases: After a user is invited to a group, the success or failure can be determined by the callback.
///
/// When to call /Trigger: Results are returned after the user is invited to the group.
///
/// Related API:[inviteUsersIntoGroup] invites users to join the group.
typedef void (^ZIMGroupUsersInvitedCallback)(NSString *groupID,
                                             NSArray<ZIMGroupMemberInfo *> *userList,
                                             NSArray<ZIMErrorUserInfo *> *errorUserList,
                                             ZIMError *errorInfo);

/// Description: Returns the result of the kick out group member operation.
///
/// Use cases: After a group member is kicked out, the success or failure can be determined by the callback.
///
/// When to call /Trigger: The result is returned after the group member is kicked out.
///
/// Related API:[kickGroupMembers] Kick out group members.
typedef void (^ZIMGroupMemberKickedCallback)(NSString *groupID,
                                             NSArray<NSString *> *kickedUserIDList,
                                             NSArray<ZIMErrorUserInfo *> *errorUserList,
                                             ZIMError *errorInfo);

/// Description: Returns the result of the group master transfer operation.
///
/// Use cases: After a group master transfer operation is performed, the success of the operation can be determined by this callback.
///
/// When to call /Trigger: The result of the group master transfer operation is returned.
///
/// Related API:[transferGroupOwner], group master transfer.
typedef void (^ZIMGroupOwnerTransferredCallback)(NSString *groupID, NSString *toUserID,
                                                 ZIMError *errorInfo);

/// Description: Return result of group name update operation.
///
/// Use cases: After a group name update operation is performed, the success or failure can be determined by this callback.
///
/// When to call /Trigger: The result of the group name update operation is returned.
///
/// Related API:[updateGroupName], the group name is updated.
typedef void (^ZIMGroupNameUpdatedCallback)(NSString *groupID, NSString *groupName,
                                            ZIMError *errorInfo);

typedef void (^ZIMGroupAvatarUrlUpdatedCallback)(NSString *groupID, NSString *groupAvatarUrl,
                                                 ZIMError *errorInfo);

/// Description: Return result of group name update operation.
///
/// Use cases: After a group name update operation is performed, the success or failure can be determined by this callback.
///
/// When to call /Trigger: The result of the group name update operation is returned.
///
/// Related API:[updateGroupName], the group name is updated.
typedef void (^ZIMGroupNoticeUpdatedCallback)(NSString *groupID, NSString *groupNotice,
                                              ZIMError *errorInfo);

/// Description: Returns the result of the group dismiss operation.
///
/// Use cases: After a group disband operation is performed, the success of the operation can be determined by the callback.
///
/// When to call /Trigger: The result of the group disband operation is returned.
///
/// Related API: [createGroup],creates a group. [dismissGroup],dismissGroup.
typedef void (^ZIMGroupInfoQueriedCallback)(ZIMGroupFullInfo *groupInfo, ZIMError *errorInfo);

/// Description: Returns the result of the group property operation.
///
/// Use cases: This callback tells you whether a custom property operation is successful.
///
/// When to call /Trigger: The result of the group property operation is returned.
///
/// Related API: [setGroupAttributes], set the room properties. [deleteGroupAttributes], delete the room attribute.
typedef void (^ZIMGroupAttributesOperatedCallback)(NSString *groupID,
                                                   NSArray<NSString *> *errorKeys,
                                                   ZIMError *errorInfo);

/// Description: Returns the result of group attribute query.
///
/// Use cases: This callback is used to determine the success of a custom property query.
///
/// When to call /Trigger: Group attribute query results are returned.
///
/// Related API: [queryGroupAttributes], query room attributes.
typedef void (^ZIMGroupAttributesQueriedCallback)(
    NSString *groupID, NSDictionary<NSString *, NSString *> *groupAttributes, ZIMError *errorInfo);

/// Description: Return of the result of the member role update operation.
///
/// Use cases: After a member role update operation is performed, the success or failure can be determined by this callback.
///
/// When to call /Trigger: The result of the member role update operation is returned.
///
/// Related API:[setGroupMemberRole], the member role is updated.
typedef void (^ZIMGroupMemberRoleUpdatedCallback)(NSString *groupID, NSString *forUserID,
                                                  ZIMGroupMemberRole role, ZIMError *errorInfo);

/// Description: Return result of group member nickname update operation.
///
/// Use cases: After a group member nickname update operation is performed, the success or failure can be determined by this callback.
///
/// When to call /Trigger: The result of the group member nickname update operation is returned.
///
/// Related API:[setGroupMemberNickname], the nickname of the group member is updated.
typedef void (^ZIMGroupMemberNicknameUpdatedCallback)(NSString *groupID, NSString *forUserID,
                                                      NSString *nickname, ZIMError *errorInfo);

/// Description: Return of group member query results.
///
/// Use cases: After a group member query is performed, the success or failure can be determined by this callback.
///
/// When to call /Trigger: Group member query results are returned.
///
/// Related API:[queryGroupMemberInfo], queryGroupMemberInfo.
typedef void (^ZIMGroupMemberInfoQueriedCallback)(NSString *groupID, ZIMGroupMemberInfo *userInfo,
                                                  ZIMError *errorInfo);

/// Description: Returns the group list query result.
///
/// Use cases: The success of a group list query can be determined by this callback.
///
/// When to call /Trigger: The result of the group list query is returned.
///
/// Related API:[queryGroupList] to query the group list.
typedef void (^ZIMGroupListQueriedCallback)(NSArray<ZIMGroup *> *groupList, ZIMError *errorInfo);

/// Description: Returns the result of querying the group member list.
///
/// Use cases: After querying the group member list, you can use the callback to determine whether the query is successful.
///
/// When to call /Trigger: The result is displayed after the group member list is queried.
///
/// Related API:[queryGroupMemberList], query the group member list.
typedef void (^ZIMGroupMemberListQueriedCallback)(NSString *groupID,
                                                  NSArray<ZIMGroupMemberInfo *> *userList,
                                                  unsigned int nextFlag, ZIMError *errorInfo);

typedef void (^ZIMGroupMemberCountQueriedCallback)(NSString *groupID, unsigned int count,
                                                   ZIMError *errorInfo);

typedef void (^ZIMGroupMessageReceiptMemberListQueriedCallback)(
    NSString *groupID, NSArray<ZIMGroupMemberInfo *> *userList, unsigned int nextFlag,
    ZIMError *errorInfo);

/// Supported version: 2.0.0.
///
/// Detail description: Operation callback for sending a call invitation.
///
/// Business scenario: After the operation of sending a call invitation is performed, the success or failure can be known through this callback.
///
/// Notification timing: The result is returned after the operation of sending the call invitation is completed.
///
/// Related interface: [callInvite], send a call invitation.
typedef void (^ZIMCallInvitationSentCallback)(NSString *callID, ZIMCallInvitationSentInfo *info,
                                              ZIMError *errorInfo);

/// Supported version: 2.0.0.
///
/// Detail description: The operation callback for canceling the call invitation.
///
/// Business scenario: After canceling the call invitation operation, the success or failure can be known through this callback.
///
/// Notification timing: The result is returned after the cancel call invitation operation is completed.
///
/// Related interface: [callCancel], cancel the call invitation.
typedef void (^ZIMCallCancelSentCallback)(NSString *callID, NSArray<NSString *> *errorInvitees,
                                          ZIMError *errorInfo);

/// Supported version: 2.0.0.
///
/// Detail description: The operation callback for accepting the call invitation.
///
/// Business scenario: After accepting the call invitation operation, the success or failure can be known through this callback.
///
/// Notification timing: The result will be returned after accepting the call invitation operation.
///
/// Related interface: [callAccept], accept the call invitation.
typedef void (^ZIMCallAcceptanceSentCallback)(NSString *callID, ZIMError *errorInfo);

/// Supported version: 2.0.0.
///
/// Detail description: Operation callback for rejecting the call invitation.
///
/// Business scenario: After the operation of rejecting the call invitation is performed, the success or failure can be known through this callback.
///
/// Notification timing: The result is returned after the operation of rejecting the call invitation is completed.
///
/// Related interface: [callReject], rejects the call invitation.
typedef void (^ZIMCallRejectionSentCallback)(NSString *callID, ZIMError *errorInfo);

NS_ASSUME_NONNULL_END
