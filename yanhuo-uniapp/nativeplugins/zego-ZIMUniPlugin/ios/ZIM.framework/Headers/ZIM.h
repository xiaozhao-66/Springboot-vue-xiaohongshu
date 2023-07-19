//
//  ZIM.h
//  ZIM
//
//  Copyright © 2021 Zego. All rights reserved.
//

#import "ZIMEventHandler.h"

NS_ASSUME_NONNULL_BEGIN

/// ZIM SDK main class.
///
@interface ZIM : NSObject

/// Gets the SDK's version number.
///
/// When the SDK is running, the developer finds that it does not match the expected situation and submits the problem and related logs to the ZEGO technical staff for locating. The ZEGO technical staff may need the information of the engine version to assist in locating the problem.
/// Developers can also collect this information as the version information of the engine used by the app, so that the SDK corresponding to each version of the app on the line.
///
/// Available since: 1.1.0.
///
/// Description: Get the SDK version.
///
/// Use cases:
/// 1. When the SDK is running, the developer finds that it does not match the expected situation and submits the problem and related logs to the ZEGO technical staff for locating. The ZEGO technical staff may need the information of the engine version to assist in locating the problem.
/// 2. Developers can also collect this information as the version information of the engine used by the app, so that the SDK corresponding to each version of the app on the line.
///
/// When to call : It can be called at any time.
///
/// @return SDK version.
+ (NSString *)getVersion;

+ (nullable ZIM *)getInstance NS_SWIFT_NAME(shared());

// MARK: - Main

/// Create a ZIM instance.
///
/// You need to create and initialize an ZIM instance before calling any other function.
/// The SDK supports the creation of multiple ZIM instances.
///
/// @param appID Application ID issued by ZEGO for developers, please contact ZEGO technical support to apply
+ (nullable ZIM *)createWithAppID:(unsigned int)appID
    DEPRECATED_MSG_ATTRIBUTE(
        "Deprecated since ZIM 2.3.0, please use  [createWithAppConfig] instead.");
/// Create a ZIM instance.
///
/// You need to create and initialize an ZIM instance before calling any other function.
/// The SDK supports the creation of multiple ZIM instances.
///
/// @param config Some of the configuration items created.
+ (nullable ZIM *)createWithAppConfig:(ZIMAppConfig *)config;

/// Destroy the ZIM instance.
///
/// Used to release resources used by ZIM.
- (void)destroy;

/// Set log related configuration.
///
/// @param config Log configuration object.
+ (void)setLogConfig:(ZIMLogConfig *)config;

/// Set zim advanced configuration.
/// When you need to customize the set advanced configurations, you need to call this function.
/// It must be set before calling [create] to take effect. If it is set after [create], it will take effect at the next [create].
///

+ (void)setAdvancedConfigWithKey:(NSString *)key
                           value:(NSString *)value
    // clang-format off
    NS_SWIFT_NAME(setAdvancedConfig(key:value:));
// clang-format on

/// Supported version: 1.1.0 and above.
///
/// Detailed description: Example Set the SDK cache file path. Because the SDK has a default path, it is generally not recommended that you set your own path unless there is a strong need to do so.
///
/// Default value:Android：/storage/Android/data/[packageName]/files/ZIMCaches
/// iOS：~/Library/Caches/ZIMCaches
/// macOS：（sandbox）~/Library/Containers/[Bundle ID]/Data/Library/Caches/ZIMCaches / ~/Library/Caches/ZIMCaches
/// Windows：C:\Users\[Your UserName]\AppData\[App Name]ZEGO.SDK\ZIMCaches
///
/// Call timing: It must be called before [create].
///
/// Note: If the developer calls after [create], the SDK saves the configuration until it takes effect the next time [Create] is invoked.
///
/// Related callbacks: In addition to getting the login result in the callback parameter, the developer will also receive the [onConnectionStateChanged] callback during the login request and after the login is successful/failed to determine the current user's login status.
///
/// Life cycle: Set before calling [create] and takes effect when calling [create]. If the developer does not set the new logging configuration the next time [create] is created, the previous configuration will still take effect.
///
/// Platform difference: The default path varies with platforms. Please refer to the default value.
///
/// @param config Cache configuration object.
+ (void)setCacheConfig:(ZIMCacheConfig *)config;

/// Set the event notification callbacks that need to be handled. If the eventHandler is set to [nil], all the callbacks set previously will be cleared.
///
/// @param handler Event notification callback. Developers should override callbacks to focus on specific notifications based on their own business scenarios.
- (void)setEventHandler:(nullable id<ZIMEventHandler>)handler;

/// Supported version: 2.4.0 and above.

/// Detailed description: Log in to the ZIM service. [login] is the most important step of the ZIM function. You need to log in before using any other functions.

/// Call timing: This function must be called after calling [create] to create an instance and before calling other instance functions.

/// Note: Before using ZIM's single chat, room, message sending and receiving functions, you must first call this function to log in, and the UI can be displayed to the user through the login result.

/// Privacy protection statement: remind users not to pass in sensitive information involving personal privacy in the userID parameter, including but not limited to mobile phone number, ID number, passport number, real name, etc.

/// Related callbacks: In addition to getting the login result in the callback parameter, the developer will also receive the [onConnectionStateChanged] callback during the login request and after the login is successful/failed to determine the current user's login status.
/// @param userInfo Unique ID used to identify the user. Note that the userID must be unique under the same appID, otherwise mutual kicks out will occur.
/// @param callback Callback of the result.
- (void)loginWithUserInfo:(ZIMUserInfo *)userInfo callback:(ZIMLoggedInCallback)callback;

/// Login, you must log in before using all functions.
///
/// @param userInfo Unique ID used to identify the user. Note that the userID must be unique under the same appID, otherwise mutual kicks out will occur.
/// @param token The token issued by the developer's business server, used to ensure security. The generation rules are detailed in ZEGO document website.
/// @param callback Callback of the result.
- (void)loginWithUserInfo:(ZIMUserInfo *)userInfo
                    token:(NSString *)token
                 callback:(ZIMLoggedInCallback)callback;

/// Upload log and call after setting up log path.
///
/// Description: After calling [create] to create an instance, the log report can be called.
/// @param callback Callback of the result.
- (void)uploadLog:(ZIMLogUploadedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(uploadLog(with:));
// clang-format on

/// Log out of ZIM service.
- (void)logout;

/// Update the authentication token.
///
/// @param token The token issued by the developer's business server, used to ensure security. The generation rules are detailed in ZEGO document website.
/// @param callback Callback of the result.
- (void)renewToken:(NSString *)token callback:(ZIMTokenRenewedCallback)callback;

/// Available since: 2.2.0 or above.
///
/// Description: After user logs in, calling this interface could update the user's own user name.
///
/// When to call: After the user is logged in.
///
/// Privacy reminder: Try not to pass in sensitive information involving personal privacy, including but not limited to mobile phone numbers, ID numbers, passport numbers, real names, etc.
///
/// Related callbacks: [onUserNameUpdatedCallback].
///
/// Related APIs: [updatedUserExtended] and [queryUsersInfo].
/// @param userName User name wanted to changed to.
/// @param callback The callback of the update user name.
- (void)updateUserName:(NSString *)userName callback:(ZIMUserNameUpdatedCallback)callback;

- (void)updateUserAvatarUrl:(NSString *)userAvatarUrl
                   callback:(ZIMUserAvatarUrlUpdatedCallback)callback;

/// Available since: 2.2.0 or above.
///
/// Description: After user logs in, calling this interface could update the user's own user extended data.
///
/// When to call: After the user is logged in.
///
/// Privacy reminder: Try not to pass in sensitive information involving personal privacy, including but not limited to mobile phone numbers, ID numbers, passport numbers, real names, etc.
///
/// Related callbacks: [ onUserNameUpdatedCallback ].
///
/// Related APIs: [ updatedUserExtended ] and [ queryUsersInfo ].
/// @param extendedData User extended data wanted to changed to .
/// @param callback The callback of the update user extended data.
- (void)updateUserExtendedData:(NSString *)extendedData
                      callback:(ZIMUserExtendedDataUpdatedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: Through this interface, you can query and obtain the corresponding UserInfo by userID.
///
/// When to call /Trigger: It is available only after calling [create] to create the instance and then calling [login] to login.
///
/// Related callbacks: [ZIMUsersInfoQueriedCallback]
///
/// @param userIDs userID list.
/// @param config  Configuration for user info query
/// @param callback Callback of the result.
- (void)queryUsersInfo:(NSArray<NSString *> *)userIDs
                config:(ZIMUsersInfoQueryConfig *)config
              callback:(ZIMUsersInfoQueriedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(queryUsersInfo(by:config:callback:));
// clang-format on

// MARK: - Conversation

/// Available since: 2.0.0 and above.

/// Description: This method displays the session list of the logged in user.

/// Use cases: This interface can be invoked to get the data source when you need to display an existing message session after logging in.
///
/// When to call /Trigger: Can be invoked after login.
///
/// Restrictions:There is no limit to the frequency of use, available after login, unavailable after logout.
///
/// Caution: NextConversation is the riveting point of the query message, which can be null for the first query. In subsequent query, the earliest conversation can be used as nextConversation to query earlier sessions. In paging query, Count in [ZIMConversationQueryConfig] fill each pull the number of sessions.
///
/// Related callbacks: [ZIMConversationListQueriedCallback]。

/// Related APIs: [deleteConversation] Deletes the session. [clearConversationUnreadMessageCount] clear session readings.
///
/// @param config Configuration for session queries.
/// @param callback Callback of the result.
- (void)queryConversationListWithConfig:(ZIMConversationQueryConfig *)config
                               callback:(ZIMConversationListQueriedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: This interface is invoked when a session needs to be deleted. All members in the session can invoke this interface.
///
/// Use cases: You can invoke this interface implementation to delete an entire session when it is no longer needed.
///
/// When to call /Trigger: his parameter is invoked when a session needs to be deleted and can be invoked after a ZIM instance is created. The call takes effect after login and becomes invalid after logout.
///
/// Impacts on other APIs: call success will trigger onConversationchanged callback, if the deleted session include unread message triggers [onConversationTotalUnreadMessageCountUpdated] callback.
///
/// Related callbacks: [ZIMConversationDeletedCallback]
///
/// @param conversationID conversationID.
/// @param conversationType conversationtype.
/// @param config delete the session's configuration.
/// @param callback Callback of the result.
- (void)deleteConversation:(NSString *)conversationID
          conversationType:(ZIMConversationType)conversationType
                    config:(ZIMConversationDeleteConfig *)config
                  callback:(ZIMConversationDeletedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(deleteConversation(by:conversationType:config:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: Used to clear unread for the current user target session.
///
/// Use cases: This interface is called when a chat page is entered from a session and the original message readings of the session need to be cleared.
///
/// When to call /Trigger: Called when a target needs to be cleared without readings.
///
/// Restrictions: Valid after login, invalid after logout.
///
/// Impacts on other APIs: Calling this method will trigger a total readings not updated callback [conversationTotalUnreadMessageCountUpdated], would trigger a session to update callbacks [conversationChanged].
///
/// Related callbacks:[ZIMConversationUnreadMessageCountClearedCallback].
///
/// Related APIs:[conversationTotalUnreadMessageCountUpdated]、[conversationChanged].
///
/// @param conversationID conversationID.
/// @param conversationType conversation type.
/// @param callback Callback of the result.
- (void)clearConversationUnreadMessageCount:(NSString *)conversationID
                           conversationType:(ZIMConversationType)conversationType
                                   callback:
                                       (ZIMConversationUnreadMessageCountClearedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(clearConversationUnreadMessageCount(for:conversationType:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: This method enables DND by selecting whether the unread of the target session is updated when a message is received.
///
/// Use cases: If the user selects MESSAGE DO not Disturb (DND), the user can call the corresponding method.
///
/// Default value: Message DND is disabled by default.
///
/// When to call /Trigger: If the target session exists after login, invoke this interface if you want to enable the DND status of the target session.
///
/// Restrictions:  Valid after login, invalid after logout.
///
/// Impacts on other APIs: After the DND state is enabled, receiving messages is not triggered [conversationTotalUnreadMessageCountUpdated].
///
/// Related callbacks: [ZIMConversationNotificationStatusSetCallback].
///
/// Related APIs: [conversationTotalUnreadMessageCountUpdated].
///
/// @param status the session notification state.
/// @param conversationID  conversationID.
/// @param conversationType conversation type.
/// @param callback Callback of the result.
- (void)setConversationNotificationStatus:(ZIMConversationNotificationStatus)status
                           conversationID:(NSString *)conversationID
                         conversationType:(ZIMConversationType)conversationType
                                 callback:(ZIMConversationNotificationStatusSetCallback)callback;

/// Available since: 2.5.0 and above.
///
/// Description: Set all received receipts of the conversation to be read.
///
/// Use cases: Set all received receipt messages in the entire conversation to be read, and the sender of the message receipt in the conversation will receive the [onConversationMessageReceiptChanged] callback from ZIMEventHandler.
///
/// When to call: It can be called after login. It is recommended to call before entering the message list page. In the message list page, it is recommended to call [sendMessageReceiptsRead] to batch set the messages that need to be read.
///
/// Caution: Only single chat conversation are allowed.
///
/// Related callback: [ZIMConversationMessageReceiptReadSentCallback].
///
/// Related APIs: [sendMessageReceiptsRead], [sendMessage].
/// @param conversationID Conversation ID.
/// @param conversationType Conversation type, only Peer type is supported.
/// @param callback Set Conversation read callback.
- (void)sendConversationMessageReceiptRead:(NSString *)conversationID
                          conversationType:(ZIMConversationType)conversationType
                                  callback:(ZIMConversationMessageReceiptReadSentCallback)callback
    // clang-format off
    NS_SWIFT_NAME(sendConversationMessageReceiptRead(for:conversationType:callback:));
// clang-format on

// MARK: - Message

/// Available since: 2.0.0 and above.
/// @deprecated This API has been deprecated since 2.4.0, please use [sendMessage] instead.
///
/// Description: After this function is called, a message is sent to the specified user. At the same time, a [ZIMMessageSentCallback] callback is received, which can be used to determine whether the message is sent successfully.
///
/// Use cases: This function is used in 1V1 chat scenarios.
///
/// Call timing/Notification timing: Can be invoked after login.
///
/// Caution: Be aware of the [ZIMMessageSentCallback] callback when sending. This callback can be used to determine if the send fails for some reason.Pushconfig Is required only when the offline push function is required.
///
/// Usage limit: no more than 10 /s, available after login, unavailable after logout.
///
/// Scope of influence: Using this method triggers the [receivePeerMessage] callback of the message receiver and the [onConversationChanged] callback of the sender and receiver. If message DND is not set for the session where the message is sent, Triggers [conversationTotalUnreadMessageCountUpdated] callback.
///
/// Related callbacks:[ZIMMessageSentCallback]、[receivePeerMessage]、[onConversationChanged]、[conversationTotalUnreadMessageCountUpdated]。
///
/// Related API: [queryHistoryMessage]、[deleteMessageByConversationID]、[deleteMessage]
///
/// @param message The message to be sent.
/// @param toUserID The ID of the user who will receive the message.
/// @param config Related configuration for sending single chat messages.
/// @param callback Callback of the result.
- (void)sendPeerMessage:(ZIMMessage *)message
               toUserID:(NSString *)toUserID
                 config:(ZIMMessageSendConfig *)config
               callback:(ZIMMessageSentCallback)callback
    DEPRECATED_MSG_ATTRIBUTE("Deprecated since ZIM 2.4.0, please use [sendMessage] instead.");

/// Supported versions: 2.0.0 and above.
/// Detail description: This interface is called when a group chat message needs to be sent.
///
/// Business scenario: This interface can be used when sending group messages.
///
/// Call timing/Notification timing: This interface is called when a group chat message needs to be sent.
///
/// Usage limit: 10 times/s, available after login, unavailable after logout.
///
/// Note: pushconfig only needs to be filled in when you need to use the offline push function. The properties in ZIMMessage are read-only and do not need to be modified.
///
/// Scope of influence: Using this method will trigger the receivePeerMessage callback of the message receiver, and will trigger the onConversationChanged callback of the sender and receiver. If the session where the message is located does not have message DND set, the conversationTotalUnreadMessageCountUpdated callback will be triggered.
///
/// Related callbacks: [ZIMMessageSentCallback], [receiveGroupMessage], [onConversationChanged], [conversationTotalUnreadMessageCountUpdated].
///
/// Related interfaces: [queryHistoryMessage], [deleteMessageByConversationID], [deleteMessage]
///
/// @param message The message to be sent.
/// @param toGroupID The ID of the user who will receive the message.
/// @param config Related configuration for sending single chat messages.
/// @param callback Callback of the result.
- (void)sendGroupMesage:(ZIMMessage *)message
              toGroupID:(NSString *)toGroupID
                 config:(ZIMMessageSendConfig *)config
               callback:(ZIMMessageSentCallback)callback
    DEPRECATED_MSG_ATTRIBUTE("Deprecated since ZIM 2.4.0, please use [sendMessage] instead.");

/// Send room messages.
///
/// @deprecated This API has been deprecated since 2.4.0, please use [sendMessage] instead.
/// Available since: 1.1.0 or above
///
/// Description: When this function is called, the message will be sent in the room. At the same time, the [ZIMMessageSentCallback] callback will be received, which can be used to determine whether the message was sent successfully.
///
/// Use Cases: This feature is required for scenarios where multiple people in the room are chatting.
///
/// @param message The message to be sent.
/// @param toRoomID The ID of the room which will receive the message.
/// @param config Related configuration for sending room messages.
/// @param callback Callback of the result.
- (void)sendRoomMessage:(ZIMMessage *)message
               toRoomID:(NSString *)toRoomID
                 config:(ZIMMessageSendConfig *)config
               callback:(ZIMMessageSentCallback)callback
    DEPRECATED_MSG_ATTRIBUTE("Deprecated since ZIM 2.4.0, please use [sendMessage] instead.");

///Supported versions: 2.4.0 and above.
///
///Detailed description: This method can be used to send messages in single chat, room and group chat.
///
///Business scenario: When you need to send message to the target user, target message room, and target group chat after logging in, send it through this interface.
///
///Call timing/Notification timing: It can be called after login.
///
///Usage limit: no more than 10/s, available after login, unavailable after logout.
///
///Related callback: [ZIMMessageSentCallback], [ZIMMessageSendNotification], [onReceivePeMessage], [onReceiveRoomMessage], [onReceiveGroupMessage], [onConversationChanged], [onConversationTotalUnreadMessageCountUpdated].
///
///Related interfaces: [queryHistoryMessage], [deleteAllMessage], [deleteMessages],[sendMediaMessage]
///@param message The message to be sent.
///@param toConversationID The conversation ID the message needs to be sent.
///@param conversationType Conversation type, supports single chat, room and group chat.
///@param config Related configuration for sending messages.
///@param notification Related notifications when messages are sent.
///@param callback Callback of the sending result of the message.
- (void)sendMessage:(ZIMMessage *)message
    toConversationID:(NSString *)toConversationID
    conversationType:(ZIMConversationType)conversationType
              config:(ZIMMessageSendConfig *)config
        notification:(nullable ZIMMessageSendNotification *)notification
            callback:(ZIMMessageSentCallback)callback
    // clang-format off
    NS_SWIFT_NAME(sendMessage(_:toConversationID:conversationType:config:notification:callback:));
// clang-format on

/// Send media messages.
///
/// Supported versions: 2.1.0 and above.
///
/// Detailed description: This method can be used to send messages in single chat, room and group chat.
///
/// Business scenario: When you need to send media to the target user, target message room, and target group chat after logging in, send it through this interface.
///
/// Call timing/Notification timing: It can be called after login.
///
/// Usage limit: no more than 10/s, available after login, unavailable after logout.
///
/// Related: [ZIMMessageSentCallback], [ZIMMediaUploadingProgress], [onReceivePeMessage], [onReceiveRoomMessage], [onReceiveGroupMessage], [onConversationChanged], [onConversationTotalUnreadMessageCountUpdated].
///
/// Related interfaces: [queryHistoryMessage], [deleteAllMessage], [deleteMessages]
///
/// @param message The message to be sent.
/// @param toConversationID The ID of the conversation which will receive the message.
/// @param conversationType The type of the conversation which will receive the message.
/// @param config Related configuration for sending single chat messages.
/// @param progress Callback of the progress.
/// @param callback Callback of the result.
- (void)sendMediaMessage:(ZIMMediaMessage *)message
        toConversationID:(NSString *)toConversationID
        conversationType:(ZIMConversationType)conversationType
                  config:(ZIMMessageSendConfig *)config
                progress:(ZIMMediaUploadingProgress)progress
                callback:(ZIMMessageSentCallback)callback
    DEPRECATED_MSG_ATTRIBUTE(
        "Deprecated since ZIM 2.4.0, please use another [sendMediaMessage] instead")
    // clang-format off
    NS_SWIFT_NAME(sendMediaMessage(_:toConversationID:conversationType:config:progress:callback:));
// clang-format on

/// Supported versions: 2.4.0 and above.
///
/// Detailed description: This method can be used to send messages in single chat, room and group chat.
///
/// Business scenario: When you need to send media to the target user, target message room, and target group chat after logging in, send it through this interface.
///
/// Call timing/Notification timing: It can be called after login.
///
/// Usage limit: no more than 10/s, available after login, unavailable after logout.
///
/// Impact: [onReceivePeerMessage]/[ReceiveGroupMessage] sessions and session-scoped [onReceiveGroupMessage] sessions did not fire message receiver's [ConversationR] fires [onversationTotalUnreadMessageCountUpdated] objection.
///
/// Note: Only required if you need to use the threaded update feature when pushing configuration. Push notifications are not supported, nor are [onContationChanged] and [ConTotalUnreadMessageCountUpdated] supported if media messages are broadcast to the world.
///
/// Related: [ZIMMessageSentCallback], [ZIMMediaMessageSendNotification], [onReceivePeMessage], [onReceiveRoomMessage], [onReceiveGroupMessage], [onConversationChanged], [onConversationTotalUnreadMessageCountUpdated].
///
/// Related interfaces: [queryHistoryMessage], [deleteAllMessage], [deleteMessages]
/// @param message When using the message to be sent, modify the type of message according to the type of multimedia message. For example, when sending image messages, use ZIMImageMessage.
/// @param toConversationID The conversation ID of the message recipient, supports single chat, room and group chat.
/// @param conversationType Conversation type, supports single chat, room and group chat.
/// @param config Related configuration for sending messages.
/// @param notification Relevant notifications when sending media messages, including upload progress, etc.
/// @param callback Result callback for sending media messages.
- (void)sendMediaMessage:(ZIMMediaMessage *)message
        toConversationID:(NSString *)toConversationID
        conversationType:(ZIMConversationType)conversationType
                  config:(ZIMMessageSendConfig *)config
            notification:(nullable ZIMMediaMessageSendNotification *)notification
                callback:(ZIMMessageSentCallback)callback
    // clang-format off
    NS_SWIFT_NAME(sendMediaMessage(_:toConversationID:conversationType:config:notification:callback:));
// clang-format on

/// Download media message content.
///
/// Supported versions: 2.1.0 and above.
///
/// Detailed description: This method can be used to download the content of media messages, including the original image, large image, thumbnail image, file message, audio message, video message and the first frame of the image message.
///
/// Service scenario: After the user receives a message, if the message is a media message, he can call this API to download its content.
///
/// Invoke timing/notification timing: can be invoked after logging in and receiving a media message.
///
/// Related callbacks: [ZIMMediaDownloadedCallback], [ZIMMediadownloadingProgress].
/// @param message The message to be received.
/// @param fileType Media file type
/// @param progress Callback of the progress.
/// @param callback Callback of the result.
- (void)downloadMediaFileWithMessage:(ZIMMediaMessage *)message
                            fileType:(ZIMMediaFileType)fileType
                            progress:(ZIMMediaDownloadingProgress)progress
                            callback:(ZIMMediaDownloadedCallback)callback;

/// Supported versions: 2.0.0 and above.

/// Detailed description: This method is used to query historical messages.
///
/// Business scenario: When you need to obtain past historical messages, you can call this interface to query historical messages by paging.
///
/// Call timing/Notification timing: Called when historical messages need to be queried.
///
/// Restrictions: Effective after login, invalid after logout.
///
/// Related callbacks: [ZIMMessageQueriedCallback]
///
/// @param conversationID The session ID of the queried historical message.
/// @param conversationType The type of the queried historical message.
/// @param config  Query the configuration of historical messages.
/// @param callback Callback of the result.
- (void)queryHistoryMessageByConversationID:(NSString *)conversationID
                           conversationType:(ZIMConversationType)conversationType
                                     config:(ZIMMessageQueryConfig *)config
                                   callback:(ZIMMessageQueriedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(queryHistoryMessage(by:conversationType:config:callback:));
// clang-format on

/// Supported versions: 2.0.0 and above.
///
/// Detail description: When you need to delete all messages under the target session, call this method.
///
/// Business scenario: If you want to implement a group setting page to clear the chat information under the current session, you can call this interface.
///
/// Call timing/Notify timing: The target session exists and the user is a member of this session.
///
/// Restrictions: Effective after login, invalid after logout.
///
/// Note: The impact of deleting messages is limited to this account, and messages from other accounts will not be deleted.
///
/// Scope of influence: The [conversationChanged] callback is triggered, and if there are unread messages, the [conversationTotalUnreadMessageCountUpdated] callback is triggered.
///
/// Related callback: [ZIMMessageDeletedCallback].
///
/// @param conversationID The session ID of the message to be deleted.
/// @param conversationType  conversation type.
/// @param config delete session configuration.
/// @param callback Callback of the result.
- (void)deleteAllMessageByConversationID:(NSString *)conversationID
                        conversationType:(ZIMConversationType)conversationType
                                  config:(ZIMMessageDeleteConfig *)config
                                callback:(ZIMMessageDeletedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(deleteAllMessage(by:conversationType:config:callback:));
// clang-format on

/// Supported versions: 2.0.0 and above.
///
/// Detail description: This method implements the function of deleting messages.
///
/// Business scenario: The user needs to delete a message. When the user does not need to display a message, this method can be used to delete it.
///
/// Call timing/Notification timing: Called when the message needs to be deleted.
///
/// Note: The impact of deleting messages is limited to this account.
///
/// Restrictions: Effective after login, invalid after logout.
///
/// Scope of influence: If the deleted message is the latest message of the session, the [conversationChanged] callback will be triggered, and if the message is unread, the [conversationTotalUnreadMessageCountUpdated] callback will be triggered.
///
/// @param messageList List of deleted messages.
/// @param conversationID conversation ID.
/// @param conversationType conversation type.
/// @param config  Delete the configuration of the message.
/// @param callback Callback of the result.
- (void)deleteMessages:(NSArray<ZIMMessage *> *)messageList
        conversationID:(NSString *)conversationID
      conversationType:(ZIMConversationType)conversationType
                config:(ZIMMessageDeleteConfig *)config
              callback:(ZIMMessageDeletedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(deleteMessages(_:conversationID:conversationType:config:callback:));
// clang-format on

/// Supported Versions: 2.4.0 and above.
///
/// Detail description: This method can insert a message directly to the local DB on the client side.
///
/// Business scenario: The developer can combine the system message type, and convert the callback notification (for example, invite someone into the group, remove someone from the group, etc.) to the system message type on the client side and insert it into the local DB to achieve the effect of the system prompt .
///
/// Call timing/Notification timing: It can be called after login.
///
/// Related callback: [ZIMMessageInsertedCallback]
///
/// Related interfaces: [queryHistoryMessage], [deleteAllMessage], [deleteMessages]
/// @param message The message to be insert.
/// @param conversationID Conversation ID.
/// @param conversationType Conversation type.
/// @param senderUserID The sender ID of this message.
/// @param callback The result callback of the inserted message.
- (void)insertMessageToLocalDB:(ZIMMessage *)message
                conversationID:(NSString *)conversationID
              conversationType:(ZIMConversationType)conversationType
                  senderUserID:(NSString *)senderUserID
                      callback:(ZIMMessageInsertedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(insertMessageToLocalDB(_:conversationID:conversationType:senderUserID:callback:));
// clang-format on

/// Available since: 2.5.0 and above.
///
/// Description: This method can set the receipt of a batch of messages to become read.
///
/// Use cases: Developers can use this method to set a batch of messages with receipts that have been read. If the sender is online, it will receive the [onMessageReceiptChanged] callback.
///
/// When to call: Callable after login. It is recommended to set the settings for the messages that need to be read on the message list page. It is not recommended to mix with [sendConversationMessageReceiptRead].
///
/// Restrictions: Only support the settings for received messages with receipt status as PROCESSING.
///
/// Related callbacks: [ZIMMessageReceiptsReadSentCallback].
///
/// Related APIs: [sendMessage].
/// @param messageList To set the list of read messages.
/// @param conversationID  Conversation ID.
/// @param conversationType Conversation type.
/// @param callback Set the result callback of the read message.
- (void)sendMessageReceiptsRead:(NSArray<ZIMMessage *> *)messageList
                 conversationID:(NSString *)conversationID
               conversationType:(ZIMConversationType)conversationType
                       callback:(ZIMMessageReceiptsReadSentCallback)callback;

/// Available since: 2.5.0 and above.
///
/// Description: This method can query the receipt information of a batch of messages, including the status, the number of unread users and the number of read users.
///
/// Use cases: If you need to query the receipt status of the message, the number of unread users and the number of read users, you can call this interface.
///
/// When to call: Callable after login. If you need to query the detailed member list, you can query through the interface [queryGroupMessageReceiptReadMemberList] or [queryGroupMessageReceiptUnreadMemberList].
///
/// Restrictions: Only messages whose statuses are not NONE and UNKNOWN are supported.
///
/// Related callbacks: [ZIMMessageReceiptsInfoQueriedCallback].
///
/// Related APIs: [queryGroupMessageReceiptReadMemberList] , [queryGroupMessageReceiptUnreadMemberList].
/// @param messageList list of messages to query.
/// @param conversationID Conversation ID.
/// @param conversationType Conversation type.
/// @param callback Callback for the result of querying message receipt information.
- (void)queryMessageReceiptsInfoByMessageList:(NSArray<ZIMMessage *> *)messageList
                               conversationID:(NSString *)conversationID
                             conversationType:(ZIMConversationType)conversationType
                                     callback:(ZIMMessageReceiptsInfoQueriedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(queryMessageReceiptsInfo(by:conversationID:conversationType:callback:));
// clang-format on

/// Available sinces: 2.5.0 and above.
///
/// Detail description: This method implements the function of revoking messages.
///
/// Use cases: The user needs to recall a message. This method can be used when the user does not want other users to see the message.
///
/// When to call: Called when the message needs to be revoked.
///
/// Note: Room message revoke is not supported.
///
/// Restrictions: Effective after login.
///
/// Related callbacks: If the revoked message is the latest message of the session, the [conversationChanged] callback will be triggered, and if the message is unread, the [conversationTotalUnreadMessageCountUpdated] callback will be triggered.
/// Scope of influence: If the revoked message is the latest message of the session, the [conversationChanged] callback will be triggered, and if the message is unread, the [conversationTotalUnreadMessageCountUpdated] callback will be triggered.
/// @param message The message needs to be revok.
/// @param config Revoke the configuration of the message.
/// @param callback Returns the result of revoking the message.
- (void)revokeMessage:(ZIMMessage *)message
               config:(ZIMMessageRevokeConfig *)config
             callback:(ZIMMessageRevokedCallback)callback;
// MARK: - Room

/// Create a room.
///
/// Available since: 1.1.0 or above.
///
/// Description: When a room is created, other users can join this room through [joinRoom] function.
///
/// Use cases: When you need to create a multi-person chat scene, you can create a room by this API.
///
/// When to call: It can be called after creating a ZIM instance through [create].
///
/// Caution: When everyone leaves the room, the room will be automatically destroyed.
///
/// Related callbacks: The result of the room creation can be obtained through the [ZIMRoomCreatedCallback] callback.
///
/// Related APIs: You can join the room through [joinRoom] and leave the room with [leaveRoom].
///
/// @param roomInfo The configuration information of the room to be created.
/// @param callback Callback of the result.
- (void)createRoom:(ZIMRoomInfo *)roomInfo
          callback:(ZIMRoomCreatedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(createRoom(with:callback:));
// clang-format on

/// Create a room.
///
/// Available since: 1.3.0.
///
/// Description: When a room is created, other users can join this room through [joinRoom] function.
///
/// Use cases: When you need to create a multi-person chat scene, you can create a room by this API.
///
/// When to call: It can be called after creating a ZIM instance through [create].
///
/// Caution: When everyone leaves the room, the room will be automatically destroyed.
///
/// Related callbacks: The result of the room creation can be obtained through the [ZIMRoomCreatedCallback] callback.
///
/// Related APIs: You can join the room through [joinRoom] and leave the room with [leaveRoom].
///
/// @param roomInfo The configuration information of the room to be created.
/// @param config The advanced information of the room to be created.
/// @param callback Callback of the result.
- (void)createRoom:(ZIMRoomInfo *)roomInfo
            config:(ZIMRoomAdvancedConfig *)config
          callback:(ZIMRoomCreatedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(createRoom(with:config:callback:));
// clang-format on

/// Join a room.
///
/// Available since: 1.1.0 or above.
///
/// Description: If the room does not exist, the join fails and you need to call [createRoom] to create the room first.
///
/// Use cases: In a multi-person chat scenario, users can call this interface to enter the room when they need to join the room.
///
/// When to call: It can be called after creating a ZIM instance through [create].
///
/// Caution: When everyone leaves the room, the room will be automatically destroyed.
///
/// Related callbacks: The result of joining the room can be obtained through the [ZIMRoomJoinedCallback] callback.
///
/// Related APIs: You can create a room with [createRoom] and leave the room with [leaveRoom].
///
/// @param roomID ID of the room to join.
/// @param callback Callback of the result of joining the room.
- (void)joinRoom:(NSString *)roomID
        callback:(ZIMRoomJoinedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(joinRoom(by:callback:));
// clang-format on

- (void)enterRoom:(ZIMRoomInfo *)roomInfo
           config:(nullable ZIMRoomAdvancedConfig *)config
         callback:(ZIMRoomEnteredCallback)callback
    // clang-format off
NS_SWIFT_NAME(enterRoom(with:config:callback:));
// clang-format on

/// Leave a room.
///
/// Available since: 1.1.0 or above.
///
/// Description: When users in the room need to leave the room, they can join this room through [leaveRoom].
///
/// Use cases: In the multi-person chat scenario, when users in the room need to leave the room, they can leave the room through this interface.
///
/// When to call: After creating a ZIM instance via [create], it can be called when the user is in the room.
///
/// Caution: If the current user is not in this room, the exit fails. When everyone leaves the room, the room will be automatically destroyed.
///
/// Related callbacks: The result of leaving the room can be obtained through the [ZIMRoomLeftCallback] callback.
///
/// Related APIs: You can create a room through [createRoom] and join a room with [joinRoom].
///
/// @param roomID ID of the room to leave.
/// @param callback Callback of the result of joining the room.
- (void)leaveRoom:(NSString *)roomID
         callback:(ZIMRoomLeftCallback)callback
    // clang-format off
NS_SWIFT_NAME(leaveRoom(by:callback:));
// clang-format on

/// Query the list of members in the room.
///
/// Available since: 1.1.0 or above.
///
/// Description: After joining a room, you can use this function to get the list of members in the room.
///
/// Use cases: When a developer needs to obtain a list of room members for other business operations, this interface can be called to obtain a list of members.
///
/// When to call: After creating a ZIM instance through [create], and the user is in the room that needs to be queried, you can call this interface.
///
/// Caution: If the user is not currently in this room, the query fails.
///
/// Related callbacks: Through the [ZIMRoomMemberQueriedCallback] callback, you can get the result of querying the room member list.
///
/// Related APIs: You can check the online number of people in the room through [queryRoomOnlineMemberCount].
///
/// @param roomID ID of the room to query.
/// @param config Configuration of query room member operation.
/// @param callback Callback for the result of querying room members list.
- (void)queryRoomMemberListByRoomID:(NSString *)roomID
                             config:(ZIMRoomMemberQueryConfig *)config
                           callback:(ZIMRoomMemberQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryRoomMemberList(by:config:callback:));
// clang-format on

/// Query the number of online members in the room.
///
/// Available since: 1.1.0 or above.
///
/// Description: After joining a room, you can use this function to get the number of online members in the room.
///
/// Use cases: When a developer needs to obtain the number of room members who are online, this interface can be called.
///
/// Calling time: After creating a ZIM instance through [create], and the user is in the room that needs to be queried, this interface can be called.
///
/// Caution: If the user is not currently in this room, the query will fail.
///
/// Related callbacks: The result of querying the online number of room members can be obtained through the [ZIMRoomOnlineMemberCountQueriedCallback] callback.
///
/// Related APIs: the room member can be inquired through [queryRoomMember].
///
/// @param roomID ID of the room to query.
/// @param callback Callback for the result of querying room online members count.
- (void)queryRoomOnlineMemberCountByRoomID:(NSString *)roomID
                                  callback:(ZIMRoomOnlineMemberCountQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryRoomOnlineMemberCount(by:callback:));
// clang-format on

/// Set room attributes (use this for all additions and changes).
///
/// Available since: 1.3.0.
///
/// Description: Used to set room properties.
///
/// @param roomAttributes room attributes will be set.
/// @param roomID ID of the room to set.
/// @param config config of the room to set.
/// @param callback Callback for the result of setting room attributes.
- (void)setRoomAttributes:(NSDictionary<NSString *, NSString *> *)roomAttributes
                   roomID:(NSString *)roomID
                   config:(nullable ZIMRoomAttributesSetConfig *)config
                 callback:(ZIMRoomAttributesOperatedCallback)callback
    // clang-format off
NS_SWIFT_NAME(setRoomAttributes(_:roomID:config:callback:));
// clang-format on

/// Delete room attributes.
///
/// Available since: 1.3.0.
///
/// Description: Used to delete room attributes.
///
/// @param keys room attributes keys will be deleted.
/// @param roomID ID of the room to deleted.
/// @param config config of the room to deleted.
/// @param callback Callback for the result of operation room attributes.
- (void)deleteRoomAttributesByKeys:(NSArray<NSString *> *)keys
                            roomID:(NSString *)roomID
                            config:(nullable ZIMRoomAttributesDeleteConfig *)config
                          callback:(ZIMRoomAttributesOperatedCallback)callback
    // clang-format off
NS_SWIFT_NAME(deleteRoomAttributes(by:roomID:config:callback:));
// clang-format on

/// Open combination room attribute operation.
///
/// Available since: 1.3.0.
///
/// Description: Used to turn on the combination of room attributes.
///
/// @param roomID ID of the room to operation.
/// @param config config of the room to turn on the combination of room attributes.
- (void)beginRoomAttributesBatchOperationWithRoomID:(NSString *)roomID
                                             config:
                                                 (nullable ZIMRoomAttributesBatchOperationConfig *)
                                                     config
    // clang-format off
NS_SWIFT_NAME(beginRoomAttributesBatchOperation(with:config:));
// clang-format on

/// Complete the property operation of the combined room.
///
/// Available since: 1.3.0.
///
/// Description: After completing the operation of combining room attributes,
/// all the setting/deleting operations from the last call to beginRoomAttributesBatchOperation
/// to this operation will be completed for the room.
///
/// @param roomID ID of the room to operation.
/// @param callback Callback for the result of operation combination of room attributes.
- (void)endRoomAttributesBatchOperationWithRoomID:(NSString *)roomID
                                         callback:(ZIMRoomAttributesBatchOperatedCallback)callback
    // clang-format off
NS_SWIFT_NAME(endRoomAttributesBatchOperation(with:callback:));
// clang-format on

/// Query all properties of the room.
///
/// Available since: 1.3.0.
///
/// Used to query room attributes.
///
/// @param roomID ID of the room to queried.
/// @param callback Callback for the result of quering room attributes.
- (void)queryRoomAllAttributesByRoomID:(NSString *)roomID
                              callback:(ZIMRoomAttributesQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryRoomAllAttributes(by:callback:));
// clang-format on

/// Available since:2.4.0 or later.
///
/// Description:Call this API to batch query the room user attributes of the members in the room.
///
/// Use cases:Use this interface when you need to specify that you want to query some room users.
///
/// Restrictions:The maximum call frequency is 5 times within 30 seconds by default, and the maximum query time is 100 people.
///
/// Related callbacks:[ZIMRoomMembersAttributesQueriedCallback]
///
/// Related APIs: [setRoomMembersAttributes]、[queryRoomMemberAttributesList]
///
/// Runtime lifecycle: It is available after logging in and joining the corresponding room, but unavailable after leaving the corresponding room.
///
/// @param userIDs A list of userIDs to query.
/// @param roomID  Room ID.
/// @param callback Callback for the result of batch query of room user attributes.
- (void)queryRoomMembersAttributesByUserIDs:(NSArray<NSString *> *)userIDs
                                     roomID:(NSString *)roomID
                                   callback:(ZIMRoomMembersAttributesQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryRoomMembersAttributes(by:roomID:callback:));
// clang-format on

/// Available since:2.4.0 or later.
///
/// Description:Call the API to paginate the room user properties that have room property members in the room.
///
/// Use cases:This interface is used when you need to query all room users.
///
/// Restrictions:The maximum call frequency is 5 times within 30 seconds by default, and the maximum query time is 100 people.
///
/// Related callbacks:[ZIMRoomMemberAttributesListQueriedCallback]
///
/// Related APIs: [setRoomMembersAttributes]、[queryRoomMembersAttributes]
///
/// Runtime lifecycle: It is available after logging in and joining the corresponding room, but unavailable after leaving the corresponding room.
///
/// @param roomID  Room ID.
/// @param config  Behavior configuration of the operation.
/// @param callback Result callback for querying member attributes in the room.
- (void)queryRoomMemberAttributesListByRoomID:(NSString *)roomID
                                       config:(ZIMRoomMemberAttributesQueryConfig *)config
                                     callback:(ZIMRoomMemberAttributesListQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryRoomMemberAttributesList(by:config:callback:));
// clang-format on

/// Supported Versions: 2.4.0 and above.
///
/// Detail description: Call this API to set room user properties of members in the room.
///
/// Business scenario: If you need to set a level for members in the room, you can use this interface to set a state.
///
/// Default: [ZIMRoomMemberAttributesSetConfig] Default constructor isDeleteAfterOwnerLeft is true.
///
/// Call timing/Notification timing: After logging in and calling in the relevant room.
///
/// Usage limit: background limit, default 20
///
/// Relevant callback: [ZIMRoomMembersAttributesOperatedCallback],[onRoomMemberAttributesUpdated]
///
/// Related interfaces: [queryRoomMembersAttributes], [queryRoomMemberAttributesList].
/// @param attributes Room member attributes to be set.
/// @param userIDs A list of userIDs to set.
/// @param roomID Room ID.
/// @param config Behavior configuration of the operation.
/// @param callback Action callback for setting room members attributes.
- (void)setRoomMembersAttributes:(NSDictionary<NSString *, NSString *> *)attributes
                         userIDs:(NSArray<NSString *> *)userIDs
                          roomID:(NSString *)roomID
                          config:(ZIMRoomMemberAttributesSetConfig *)config
                        callback:(ZIMRoomMembersAttributesOperatedCallback)callback;

// MARK: - Group
/// Available since: 2.0.0 and above.
///
/// Description: You can call this interface to create a group, and the person who calls this interface is the group leader. An empty string if the group name is left blank.
///
/// Use cases: You can use this interface to create a chat scenario and join a group.
///
/// When to call: After you create a ZIM instance with [create] and login with [login].
///
/// Restrictions: Available after login, unavailable after logout. UserIDs support a maximum of 100 users and a group supports a maximum of 500 users.
///
/// Impacts on other APIs: You can use [joinGroup] to join a group, [leaveGroup] to leave a group, or [dismissGroup] to dismiss a group.
///
/// Related callbacks: The result of creating the group is obtained through the [ZIMGroupCreatedCallback] callback.
///
/// @param groupInfo Configuration information for the group to be created.
/// @param userIDs List of users invited to the group.
/// @param callback Callback for the result of creating a group.
- (void)createGroup:(ZIMGroupInfo *)groupInfo
            userIDs:(NSArray<NSString *> *)userIDs
           callback:(ZIMGroupCreatedCallback)callback
    // clang-format off
NS_SWIFT_NAME(createGroup(with:userIDs:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: You can call this interface to create a group, and the person who calls this interface is the group leader.
///
/// Use cases: You can use this interface to create a chat scenario and join a group.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Caution: Available after login, unavailable after logout. UserIDs can have a maximum of 100 users and a group can have a maximum of 500 users.
///
/// Related callbacks: The result of creating the group is obtained through the [ZIMGroupCreatedCallback] callback.
///
/// Related APIs: You can use [joinGroup] to join a group, [leaveGroup] to leave a group, or [dismissGroup] to dismiss a group.
///
/// @param groupInfo Configuration information for the group to be created.
/// @param userIDs List of users invited to the group.
/// @param config  Create the relevant configuration of the group.
/// @param callback Callback for the result of creating a group.
- (void)createGroup:(ZIMGroupInfo *)groupInfo
            userIDs:(NSArray<NSString *> *)userIDs
             config:(ZIMGroupAdvancedConfig *)config
           callback:(ZIMGroupCreatedCallback)callback
    // clang-format off
NS_SWIFT_NAME(createGroup(with:userIDs:config:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: When a group is created, you can use [dismissGroup] to dismiss it.
///
/// Use cases: After you create a chat group, you do not need to use this interface to dissolve the group.
///
/// When to call /Trigger: This parameter can be called after a group is created by using [createGroup].
///
/// Caution: A non-group owner cannot dissolve a group.
///
/// Impacts on other APIs: Through callback can get [ZIMGroupDismissedCallback] dissolution results of the room, through [onGroupStateChanged] listen callback can get the room status.
///
/// Related callbacks: You can use [createGroup] to create a group, [joinGroup] to join a group, and [leaveGroup] to leave a group.
///
/// @param groupID The ID of the group to be disbanded.
/// @param callback  Callback for the result of disbanding the group.
- (void)dismissGroup:(NSString *)groupID
            callback:(ZIMGroupDismissedCallback)callback
    // clang-format off
NS_SWIFT_NAME(dismissGroup(by:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, other users can use [joinGroup] to join the group.
///
/// Use cases: This interface is used to join a group in a chat scenario.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Caution: Available after login, unavailable after logout. If you have joined a group, the join succeeds. A group is limited to 500 people and fails to join when it is full.
///
/// Related callbacks: To get the result of joining the room, call [ZIMGroupJoinedCallback].
///
/// Related APIs: You can use [createGroup] to create a group, [leaveGroup] to leave a group, or [dismissGroup] to dismiss a group.
///
/// @param groupID The group ID to join.
/// @param callback  Callback for the result of joining the group.
- (void)joinGroup:(NSString *)groupID
         callback:(ZIMGroupJoinedCallback)callback
    // clang-format off
NS_SWIFT_NAME(joinGroup(by:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a user joins a group, the user can leave the group through this interface.
///
/// Use cases: This interface is used to exit a chat group.
///
/// When to call /Trigger: It can be invoked after a ZIM instance is created through [create] and logged in.
///
/// Restrictions: Available after login, unavailable after logout.
///
/// Caution: When the group owner quits the group, the identity of the group owner will be automatically transferred to the earliest member who joined the group. When all members exit the group, the group is automatically dissolved.
///
/// Impacts on other APIs: You can use [createGroup] to create a group, [joinGroup] to join a group, or [dismissGroup] to dismiss a group.
///
/// Related callbacks: The result of leaving the room can be obtained by the [ZIMGroupLeftCallback] callback.
///
/// @param groupID The group ID to leave.
/// @param callback  Callback for the result of leaving the group.
- (void)leaveGroup:(NSString *)groupID
          callback:(ZIMGroupLeftCallback)callback
    // clang-format off
NS_SWIFT_NAME(leaveGroup(by:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, users can add multiple users to the group through this interface. The interface can be invoked by both the master and members of the group.
///
/// Use cases: This interface allows you to invite others to join a group chat.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: The maximum number of userIDs users can join the group is 100. If the number of users reaches 100, the interface callback will notify the user. The maximum number of people in a group is 500.
///
/// Caution: This interface does not require the peer's consent or the peer's online status. The service layer determines the number of invited users.
///
/// Related callbacks: Through the callback [ZIMGroupUserInvitedCallback] can add multiple users into the group's results.
///
/// Related APIs: KickGroupMember can be used to kick a target user out of the group.
/// @param groupID The ID of the group that will invite users to the group.
/// @param userIDs List of invited users.
/// @param callback Callback of the result.
- (void)inviteUsersIntoGroup:(NSArray<NSString *> *)userIDs
                     groupID:(NSString *)groupID
                    callback:(ZIMGroupUsersInvitedCallback)callback
    // clang-format off
    NS_SWIFT_NAME(inviteUsersIntoGroup(with:groupID:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a user joins a group, you can use this method to remove the user from the group.
///
/// Use cases: You can use this method to remove one or more users from the group.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: You can't kick someone unless you're the leader of the group.
///
/// Caution: This interface does not require the peer's consent or the peer's online status. It cannot accept group-related callbacks after being kicked out. History messages and sessions remain after being kicked out and can still enter the group.
///
/// Related callbacks: Through the callback [ZIMGroupMemberKickedCallback] can get the user kicked out the results of the group.
///
/// Related APIs: You can invite a target user into a group through [inviteUsersIntoGroup].
///
/// @param groupID The group ID of the member who will be kicked out.
/// @param userIDs List of users who have been kicked out of the group.
/// @param callback Callback for the result of being kicked out of the group.
- (void)kickGroupMembers:(NSArray<NSString *> *)userIDs
                 groupID:(NSString *)groupID
                callback:(ZIMGroupMemberKickedCallback)callback
    // clang-format off
NS_SWIFT_NAME(kickGroupMembers(by:groupID:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, the group owner can use this method to assign the group owner to a specified user.
///
/// Use cases: In a group chat scenario, you can transfer the group master through this interface.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: You cannot transfer a group owner if you are not a group owner.
///
/// Related APIs: Through the callback [ZIMGroupOwnerTransferredCallback] can get the result of the transfer of the group manager.
/// @param toUserID The converted group owner ID.
/// @param groupID The group ID of the group owner to be replaced.
/// @param callback The callback of the transfer group owner.
- (void)transferGroupOwnerToUserID:(NSString *)toUserID
                           groupID:(NSString *)groupID
                          callback:(ZIMGroupOwnerTransferredCallback)callback
    // clang-format off
NS_SWIFT_NAME(transferGroupOwner(to:groupID:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, users can call this method to change the group name.
///
/// Use cases: After creating a group, you need to change the group name.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Group members and group owners can change the group name. The maximum length of the name is 100 bytes.
///
/// Related APIs: Through the callback [ZIMGroupNameUpdatedCallback] can get the result of the change of name, through [onGroupNoticeUpdated] can get update group name information.
///
/// @param groupName The updated group name.
/// @param groupID The group ID whose group name will be updated.
/// @param callback Callback for the result of updating the group name.
- (void)updateGroupName:(NSString *)groupName
                groupID:(NSString *)groupID
               callback:(ZIMGroupNameUpdatedCallback)callback;

- (void)updateGroupAvatarUrl:(NSString *)groupAvatarUrl
                     groupID:(NSString *)groupID
                    callback:(ZIMGroupAvatarUrlUpdatedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: When a group is created, users can use this method to update the group bulletin.
///
/// Use cases: You need to update the group bulletin in the group.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Only group members can update the group bulletin. The maximum number of bytes is 300. There is no special character limit.
///
/// Related callbacks: Through callback [ZIMGroupNoticeUpdateCallback] can get update group of the results announcement, announcement by [onGroupNoticeUpdated] can get update group information.
/// @param groupID The group ID of the group announcement that will be updated.
/// @param groupNotice Pre-updated group announcements.
/// @param callback A callback to update the results of group announcements.
- (void)updateGroupNotice:(NSString *)groupNotice
                  groupID:(NSString *)groupID
                 callback:(ZIMGroupNoticeUpdatedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: Query information about a created group.
///
/// Use cases: You need to obtain group information for display.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Related callbacks: Through the callback [ZIMGroupInfoQueriedCallback] can query the result of the group information.
///
/// @param groupID The group ID of the group information to be queried.
/// @param callback Callback for the result of querying group information.
- (void)queryGroupInfoByGroupID:(NSString *)groupID
                       callback:(ZIMGroupInfoQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryGroupInfo(by:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: If a group already exists, all users of the group can use this method to set group properties.
///
/// Use cases: Added extended field information about group description, such as group family, label, and industry category.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Only group members can set group properties.
///
/// Related callbacks: Through the callback [ZIMGroupAttributesOperatedCallback] can get the result of the set of properties.
///
/// Related APIs: [deleteGroupAttributes] can be used to deleteGroupAttributes, [queryGroupAttributes] can be used to queryGroupAttributes, [queryAllGroupAttributes] can be used to queryAllGroupAttributes.
///
/// @param groupAttributes group properties.
/// @param groupID groupID.
/// @param callback Callback of the result.
- (void)setGroupAttributes:(NSDictionary<NSString *, NSString *> *)groupAttributes
                   groupID:(NSString *)groupID
                  callback:(ZIMGroupAttributesOperatedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: When a group already exists, you can use this method to delete group attributes. Both the master and members of the interface group can be invoked.
///
/// Use cases: Deleted the extended field of the group description.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Only group members can delete group attributes.
///
/// Related callbacks: Through the callback [ZIMGroupAttributesOperatedCallback] can delete the result of the group of attributes.
///
/// Related APIs: You can use [setGroupAttributes] to setGroupAttributes, [queryGroupAttributes] to queryGroupAttributes, and [queryAllGroupAttributes] to queryAllGroupAttributes.
///
/// @param groupID  The group ID of the group attribute to be deleted.
/// @param keys The key of the group attribute to delete.
/// @param callback Callback for the result of removing the group property.
- (void)deleteGroupAttributesByKeys:(NSArray<NSString *> *)keys
                            groupID:(NSString *)groupID
                           callback:(ZIMGroupAttributesOperatedCallback)callback
    // clang-format off
NS_SWIFT_NAME(deleteGroupAttributes(by:groupID:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, you can use this method to query the specified group properties.
///
/// Use cases: You need to query the scenarios to display the specified group attributes.
///
/// When to call /Trigger: After creating a ZIM instance with [create] and logging in with [login].
///
/// Restrictions: Available after login, unavailable after logout.
///
/// Related callbacks: Through the callback [ZIMGroupAttributesQuriedCallback] can get query attributes of the specified group of results.
///
/// Related APIs: [queryAllGroupAttributes] Queries all group attributes.
///
///  @param keys The key of the group attribute to be queried.
///  @param groupID The group ID of the group attribute to be queried.
///  @param callback Callback for the result of querying group properties.
- (void)queryGroupAttributesByKeys:(NSArray<NSString *> *)keys
                           groupID:(NSString *)groupID
                          callback:(ZIMGroupAttributesQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryGroupAttributes(by:groupID:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, you can use this method to query all group attributes.
///
/// Use cases: Scenarios where all group attributes need to be queried.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Related callbacks: Through callback can get query [ZIMGroupAttributesQuriedCallback] all the results of the group of attributes.
///
/// Related APIs: [queryGroupAttributes] Queries the attributes of the specified group.
///
/// @param groupID The group ID of all group attributes to be queried.
/// @param callback Callback for querying the result of all attributes of the group.
- (void)queryGroupAllAttributesByGroupID:(NSString *)groupID
                                callback:(ZIMGroupAttributesQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryGroupAllAttributes(by:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, you can use this method to set the roles of group members.
///
/// Use cases: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// When to call /Trigger: If the primary role of a group is 1 and the default role of other members is 3, you can invoke this interface to change the role.
///
/// Caution: Non-group master unavailable.
///
/// Related callbacks: Through the callback [ZIMGroupMemberRoleUpdatedCallback] can be set up to get the results of the group of members of the role.
///
/// @param role Set of group roles.
/// @param forUserID User ID for which group role is set.
/// @param groupID The group ID of the group role to be set.
/// @param callback A callback to set the result of the group member role.
- (void)setGroupMemberRole:(ZIMGroupMemberRole)role
                 forUserID:(NSString *)forUserID
                   groupID:(NSString *)groupID
                  callback:(ZIMGroupMemberRoleUpdatedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, you can use this method to set nicknames for group members.
///
/// Use cases: Nicknames need to be set for group members.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Available after login, unavailable after logout. The owner of a group can change his or her own nickname, while the members can change only their own nickname.
///
/// Caution: A group name can contain a maximum of 100 characters.
///
/// Related callbacks: Through the callback [ZIMGroupMemberNicknameUpdatedCallback] can be set up to get the results of the group members nickname.
///
/// @param nickname  Set member nickname.
/// @param forUserID User ID for which group nickname is set.
/// @param groupID The group ID of the group member's nickname is set.
/// @param callback Callback for the result of setting the group member's nickname.
- (void)setGroupMemberNickname:(NSString *)nickname
                     forUserID:(NSString *)forUserID
                       groupID:(NSString *)groupID
                      callback:(ZIMGroupMemberNicknameUpdatedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, you can use this method to query information about a specified group member.
///
/// Use cases: You need to obtain the specified group member information for display or interaction.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Available after login, unavailable after logout.
///
/// Related callbacks: Through the callback [ZIMGroupMemberInfoQueriedCallback] can get the query specifies the result of group membership information.
///
/// @param userID User ID of the queried member information.
/// @param groupID The ID of the group whose member information will be queried.
/// @param callback Callback for the result of querying group member information.
- (void)queryGroupMemberInfoByUserID:(NSString *)userID
                             groupID:(NSString *)groupID
                            callback:(ZIMGroupMemberInfoQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryGroupMemberInfo(by:groupID:callback:));
// clang-format on

/// Available since: 2.0.0 and above.
///
/// Description: Query the list of all groups.
///
/// Use cases: You need to get a list of groups to display.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Available after login, unavailable after logout.
///
/// Related callbacks: Through the callback [ZIMGroupMemberListQueiedCallback] can get a check list of all current group results.
///
/// @param callback A callback for querying the result of the group list.
- (void)queryGroupList:(ZIMGroupListQueriedCallback)callback;

/// Available since: 2.0.0 and above.
///
/// Description: After a group is created, you can use this method to query the group member list.
///
/// Use cases: You need to obtain the specified group member list for display or interaction.
///
/// When to call /Trigger: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: Available after login, unavailable after logout.
///
/// Related callbacks: Through the callback [ZIMGroupMemberListQueriedCallback] can query the result of the group member list.
///
/// @param groupID The group ID of the group member list to be queried.
/// @param config Group member query configuration.
/// @param callback Callback for querying the list of group members.
- (void)queryGroupMemberListByGroupID:(NSString *)groupID
                               config:(ZIMGroupMemberQueryConfig *)config
                             callback:(ZIMGroupMemberListQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryGroupMemberList(by:config:callback:));
// clang-format on

/// Available since: 2.2.0 or above.
///
/// Description: Query the number of group members in a group.
///
/// When to call: The ZIM instance can be invoked after being created by [create] and logged in.
///
/// Restrictions: This function can only query the group that the user has entered.
///
/// Related callbacks: [ZIMGroupMemberCountQueriedCallback].
///
/// @param groupID The group ID of the group to be queried.
/// @param callback Callback for querying the number of groups.
- (void)queryGroupMemberCountByGroupID:(NSString *)groupID
                              callback:(ZIMGroupMemberCountQueriedCallback)callback
    // clang-format off
NS_SWIFT_NAME(queryGroupMemberCount(by:callback:));
// clang-format on

- (void)queryGroupMessageReceiptReadMemberListByMessage:(ZIMMessage *)message
                                                groupID:(NSString *)groupID
                                                 config:(ZIMGroupMessageReceiptMemberQueryConfig *)
                                                            config
                                               callback:
                                                   (ZIMGroupMessageReceiptMemberListQueriedCallback)
                                                       callback;

- (void)
    queryGroupMessageReceiptUnreadMemberListByMessage:(ZIMMessage *)message
                                              groupID:(NSString *)groupID
                                               config:
                                                   (ZIMGroupMessageReceiptMemberQueryConfig *)config
                                             callback:
                                                 (ZIMGroupMessageReceiptMemberListQueriedCallback)
                                                     callback;

// MARK: - CallInvite

/// Supported versions: 2.0.0 and above.
///
/// Detail description: When the caller initiates a call invitation, the called party can use [callAccept] to accept the call invitation or [callReject] to reject the invitation.
///
/// Business scenario: When you need to initiate a call invitation, you can create a unique callid through this interface to maintain this call invitation.
///
/// When to call: It can be called after creating a ZIM instance through [create].
///
/// Note: The call invitation has a timeout period, and the call invitation will end when the timeout period expires.
///
/// @param invitees list of invitees.
/// @param config Call Invitation Related Configuration.
/// @param callback Callback for initiating a call invitation.
- (void)callInviteWithInvitees:(NSArray<NSString *> *)invitees
                        config:(ZIMCallInviteConfig *)config
                      callback:(ZIMCallInvitationSentCallback)callback
    // clang-format off
NS_SWIFT_NAME(callInvite(with:config:callback:));
// clang-format on

/// Supported versions: 2.0.0 and above.
///
/// Detail description: After the caller initiates a call invitation, the call invitation can be canceled through this interface before the timeout period.
///
/// Business scenario: When you need to cancel the call invitation initiated before, you can cancel the call invitation through this interface.
///
/// When to call: It can be called after creating a ZIM instance through [create].
///
/// Note: Canceling the call invitation after the timeout period of the call invitation expires will fail.
///
/// @param invitees List of invitees.
/// @param callID callID.
/// @param config Cancel the related configuration of call invitation.
/// @param callback Callback for canceling a call invitation.
- (void)callCancelWithInvitees:(NSArray<NSString *> *)invitees
                        callID:(NSString *)callID
                        config:(ZIMCallCancelConfig *)config
                      callback:(ZIMCallCancelSentCallback)callback
    // clang-format off
NS_SWIFT_NAME(callCancel(with:callID:config:callback:));
// clang-format on

/// Supported versions: 2.0.0 and above.
///
/// Detail description: When the calling party initiates a call invitation, the called party can accept the call invitation through this interface.
///
/// Service scenario: When you need to accept the call invitation initiated earlier, you can accept the call invitation through this interface.
///
/// When to call: It can be called after creating a ZIM instance through [create].
///
/// Note: The callee will fail to accept an uninvited callid.
///
/// @param callID The call invitation ID to accept.
/// @param config  config.
/// @param callback Callback to accept call invitation.
- (void)callAcceptWithCallID:(NSString *)callID
                      config:(ZIMCallAcceptConfig *)config
                    callback:(ZIMCallAcceptanceSentCallback)callback
    // clang-format off
NS_SWIFT_NAME(callAccept(with:config:callback:));
// clang-format on

/// Supported versions: 2.0.0 and above.
///
/// Detail description: When the calling party initiates a call invitation, the called party can reject the call invitation through this interface.
///
/// Service scenario: When you need to reject the call invitation initiated earlier, you can use this interface to reject the call invitation.
///
/// When to call: It can be called after creating a ZIM instance through [create].
///
/// Note: The callee will fail to reject the uninvited callid.
///
/// @param callID The ID of the call invitation to be rejected.
/// @param config Related configuration for rejecting call invitations.
/// @param callback Callback for rejecting call invitations.
- (void)callRejectWithCallID:(NSString *)callID
                      config:(ZIMCallRejectConfig *)config
                    callback:(ZIMCallRejectionSentCallback)callback
    // clang-format off
NS_SWIFT_NAME(callReject(with:config:callback:));
// clang-format on

@end

NS_ASSUME_NONNULL_END
