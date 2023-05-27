
//
//  ZIMEventHandler.h
//  ZIM
//
//  Copyright © 2021 Zego. All rights reserved.
//

#import "ZIMDefines.h"

@class ZIM;

NS_ASSUME_NONNULL_BEGIN

/// Callback.
///
@protocol ZIMEventHandler <NSObject>

@optional

/// The event callback when the connection state changes.
///
/// @param zim ZIM instance.
/// @param state The current connection state after changed.
/// @param event The event that caused the connection state to change.
/// @param extendedData Extra information when the event occurs, a standard JSON format data.
- (void)zim:(ZIM *)zim
    connectionStateChanged:(ZIMConnectionState)state
                     event:(ZIMConnectionEvent)event
              extendedData:(NSDictionary *)extendedData;

// MARK: - Main

/// The callback for error information.
///
/// When an exception occurs in the SDK, the callback will prompt detailed information.
///
/// @param zim ZIM instance.
/// @param errorInfo Error information, please refer to the error codes document.
- (void)zim:(ZIM *)zim errorInfo:(ZIMError *)errorInfo;

/// A reminder callback that the token is about to expire.
///
/// @param zim ZIM instance.
/// @param second The remaining second before the token expires.
- (void)zim:(ZIM *)zim tokenWillExpire:(unsigned int)second;

// MARK: - Conversation
- (void)zim:(ZIM *)zim
    conversationChanged:(NSArray<ZIMConversationChangeInfo *> *)conversationChangeInfoList;

- (void)zim:(ZIM *)zim
    conversationTotalUnreadMessageCountUpdated:(unsigned int)totalUnreadMessageCount;

/// Received notification callback when the message receiver has read this receipt message.
///
/// Available since: 2.5.0 and above.
///
/// Description: When the message receiver has read the session, the message sender knows through this callback.
///
/// When to call /Trigger: Trigger a notification when the message receiver has read the session.
///
/// Related APIs: triggered when the peer calls via [sendConversationMessageReceiptRead].
/// @param zim ZIM instance.
/// @param infos Receipt information.
- (void)zim:(ZIM *)zim conversationMessageReceiptChanged:(NSArray<ZIMMessageReceiptInfo *> *)infos;

// MARK: - Message

/// The callback for receiving peer-to-peer message.
///
/// When receiving peer-to-peer message from other user, you will receive this callback.
///
/// @param zim ZIM instance.
/// @param messageList List of received messages.
/// @param fromUserID The user ID of the message sender.
- (void)zim:(ZIM *)zim
    receivePeerMessage:(NSArray<ZIMMessage *> *)messageList
            fromUserID:(NSString *)fromUserID;

/// The callback for receiving room message.
///
/// This callback will be triggered when new message is received in a room.
///
/// @param zim ZIM instance.
/// @param messageList List of received messages.
/// @param fromRoomID ID of the room where the message was received.
- (void)zim:(ZIM *)zim
    receiveRoomMessage:(NSArray<ZIMMessage *> *)messageList
            fromRoomID:(NSString *)fromRoomID;

- (void)zim:(ZIM *)zim
    receiveGroupMessage:(NSArray<ZIMMessage *> *)messageList
            fromGroupID:(NSString *)fromGroupID;

/// Received notification callback when some one else sends a message and then revoke a message sent by themselves.
///
/// Available since: 2.5.0 and above.
///
/// Description: This callback is received when some one else sends a message and then revoke.
///
/// When to call /Trigger: This callback occurs when a ZIM instance is created with [create] and the other user revoke a message.
///
/// Related APIs: You can revoke message to other members via [revokeMessage].
/// @param zim ZIM instance.
/// @param messageList List of received messages..
- (void)zim:(ZIM *)zim messageRevokeReceived:(NSArray<ZIMRevokeMessage *> *)messageList;

/// Received notification callback when the message receiver confirms that the message has been read.
///
/// Available since: 2.5.0 and above.
///
/// Description: When the message receiver confirms that the message has been read, the message sender knows through this callback.
///
/// When to call /Trigger: Trigger a notification when the message receiver has read the message.
///
/// Related APIs: Triggered when the peer calls via [sendMessageReceiptsRead].
/// @param zim ZIM instance.
/// @param infos Receipt information.
- (void)zim:(ZIM *)zim messageReceiptChanged:(NSArray<ZIMMessageReceiptInfo *> *)infos;

- (void)zim:(ZIM *)zim
    messageSentStatusChanged:
        (NSArray<ZIMMessageSentStatusChangeInfo *> *)messageSentStatusChangeInfoList;

// MARK: - Room

/// Callback when other members join the room.
///
/// Available since: 1.1.0 or above.
///
/// Description: After joining a room, when other members also join this room, they will receive this callback.
///
/// Use cases:When other members in the room join, this callback will be called.
///
/// When to call:  After creating a ZIM instance through [create], and the user is in a room joined by other members, you can call this interface.
///
/// Caution: If the user is not currently in this room, this callback will not be called.
///
/// Related APIs: You can use [roomMemberLeft] to receive this callback when other room members leave.
///
/// @param zim ZIM instance.
/// @param memberList List of members who joined the room.
/// @param roomID The ID of the room where this event occurred.
- (void)zim:(ZIM *)zim
    roomMemberJoined:(NSArray<ZIMUserInfo *> *)memberList
              roomID:(NSString *)roomID;

/// Callback when other members leave the room.
///
/// Available since: 1.1.0 or above.
///
/// Description: After joining a room, when other members leave the room, they will receive this callback.
///
/// Use cases: When other members in the room leave the room, this callback will be called.
///
/// When to call:  After creating a ZIM instance through [create], and the user is in the same room of other members, you can call this interface.
///
/// Caution:If the user is not currently in this room, this callback will not be called.
///
/// Related APIs: You can receive this callback when other room members join through [roomMemberJoined].
///
/// @param zim ZIM instance.
/// @param memberList List of members who left the room.
/// @param roomID The ID of the room where this event occurred.
- (void)zim:(ZIM *)zim
    roomMemberLeft:(NSArray<ZIMUserInfo *> *)memberList
            roomID:(NSString *)roomID;

/// event callback when the room connection status changes.
///
/// Available since:  1.1.0 or above.
///
/// Description:event callback when the room connection status changes.
///
/// When to call::After creating a ZIM instance through [create], you can call this interface.
///
/// Related APIs:through [tokenWillExpire], the callback will be received when the token is about to expire.
///
/// @param zim ZIM instance.
/// @param state The current room connection state after changed.
/// @param event The event that caused the room connection state to change.
/// @param extendedData Extra information when the event occurs, a standard JSON string.
- (void)zim:(ZIM *)zim
    roomStateChanged:(ZIMRoomState)state
               event:(ZIMRoomEvent)event
        extendedData:(NSDictionary *)extendedData
              roomID:(NSString *)roomID;

/// event callback when the room attributes changes.
///
/// Available since:  1.3.0.
///
/// Description:When the room attribute in the room changes, it will be notified through this callback.
///
/// @param zim ZIM instance.
/// @param updateInfo The info of the room attributes changed.
/// @param roomID The ID of the room where this event occurred.
- (void)zim:(ZIM *)zim
    roomAttributesUpdated:(ZIMRoomAttributesUpdateInfo *)updateInfo
                   roomID:(NSString *)roomID;

/// event callback when the room attributes changes.
///
/// Available since:  1.3.0.
///
/// Description:When the room attribute in the room changes, it will be notified through this callback.
///
/// @param zim ZIM instance.
/// @param updateInfo The infos of the room attributes changed.
/// @param roomID The ID of the room where this event occurred.
- (void)zim:(ZIM *)zim
    roomAttributesBatchUpdated:(NSArray<ZIMRoomAttributesUpdateInfo *> *)updateInfo
                        roomID:(NSString *)roomID;

/// event callback when room user property update.
///
/// Available since:  2.4.0.
///
/// Description: This callback will be received when a user's property in the room is changed.
///
/// @param zim ZIM instance.
/// @param infos  The infos of the room member attributes changed.
/// @param operatedInfo Room operation information.
/// @param roomID Room ID.
- (void)zim:(ZIM *)zim
    roomMemberAttributesUpdated:(NSArray<ZIMRoomMemberAttributesUpdateInfo *> *)infos
                   operatedInfo:(ZIMRoomOperatedInfo *)operatedInfo
                         roomID:(NSString *)roomID;
// MARK: - Group
/// Description: allback notification of group status change.
///
/// Use cases: Scenarios that require interaction based on the group status.
///
/// When to call /Trigger: A notification is triggered when a group is created, joined, left, or dismissed.
///
/// Related APIs: [createGroup] : creates a group. [joinGroup] : joins a group. [leaveGroup], leave the group. [dismissGroup]; dismiss the group.
///
/// @param zim ZIM instance.
/// @param state The status of the group after the change.
/// @param event Group related events.
/// @param operatedInfo Group information that has been operated.
/// @param groupInfo  The groupInfowhere the group state change occurred.
- (void)zim:(ZIM *)zim
    groupStateChanged:(ZIMGroupState)state
                event:(ZIMGroupEvent)event
         operatedInfo:(ZIMGroupOperatedInfo *)operatedInfo
            groupInfo:(ZIMGroupFullInfo *)groupInfo;

/// Description: Group name change notification callback.
///
/// Use cases: If the group name is changed, you need to synchronize the latest group name.
///
/// When to call /Trigger: The group name is changed. Procedure
///
/// Related APIs: [updateGroupName] : updates the group name.
///
///
/// @param zim ZIM instance.
/// @param groupName The updated group name.
/// @param operatedInfo Operation information after the group name is updated.
/// @param groupID The groupID where the group name update occurred.
- (void)zim:(ZIM *)zim
    groupNameUpdated:(NSString *)groupName
        operatedInfo:(ZIMGroupOperatedInfo *)operatedInfo
             groupID:(NSString *)groupID;

- (void)zim:(ZIM *)zim
    groupAvatarUrlUpdated:(NSString *)groupAvatarUrl
             operatedInfo:(ZIMGroupOperatedInfo *)operatedInfo
                  groupID:(NSString *)groupID;
/// Description: Group bulletin Change notification callback.
///
/// Use cases: If a group bulletin changes, you need to synchronize the latest bulletin content.
///
/// When to call /Trigger: The group bulletin is changed. Procedure
///
/// Related APIs: [updateGroupNotice], which updates the group notice.
///
/// @param zim ZIM instance.
/// @param groupNotice Updated group announcement.
/// @param operatedInfo The group announces the updated operation information.
/// @param groupID The groupID where the group announcement update occurred.
- (void)zim:(ZIM *)zim
    groupNoticeUpdated:(NSString *)groupNotice
          operatedInfo:(ZIMGroupOperatedInfo *)operatedInfo
               groupID:(NSString *)groupID;

/// Description: Group attribute change notification callback.
///
/// Use cases: When group attributes are changed, you need to synchronize the latest group attributes.
///
/// When to call /Trigger: Triggered when group properties are set, updated, or deleted.
///
/// Impacts on other APIs:  [setGroupAttributes] updates group attributes. [deleteGroupAttributes], delete the group attribute.
///
/// @param zim ZIM instance.
/// @param operatedInfo Operation information after the group attribute is updated.
/// @param updateInfo Information after group attribute update.
/// @param groupID The groupID for sending group attribute updates.
- (void)zim:(ZIM *)zim
    groupAttributesUpdated:(NSArray<ZIMGroupAttributesUpdateInfo *> *)updateInfo
              operatedInfo:(ZIMGroupOperatedInfo *)operatedInfo
                   groupID:(NSString *)groupID;

/// Description: Group member status change notification callback.
///
/// Use cases: Scenarios that require interaction based on group member states.
///
/// When to call /Trigger: Notification is triggered when a group is created, joined, left, or dismissed, or a user is invited to join or kicked out of the group.
///
/// Related APIs: [createGroup] : creates a group. [joinGroup] : joins a group. [leaveGroup], leave the group. [dismissGroup]; dismiss the group. [intiveUsersIntoGroup], which invites users to join the group. [kickoutGroupMember] kicks the user out of the group.
///
///
/// @param zim ZIM instance.
/// @param state Updated membership status.
/// @param event Updated member events.
/// @param userList Updated member information.
/// @param operatedInfo Updated operational information.
/// @param groupID The groupID where the member state change occurred.
- (void)zim:(ZIM *)zim
    groupMemberStateChanged:(ZIMGroupMemberState)state
                      event:(ZIMGroupMemberEvent)event
                   userList:(NSArray<ZIMGroupMemberInfo *> *)userList
               operatedInfo:(ZIMGroupOperatedInfo *)operatedInfo
                    groupID:(NSString *)groupID;

/// Description: Return the operation result of changing group member information.
///
/// Use cases: After the basic information of group members is changed, you need to display or interact with group members on the page.
///
/// When to call /Trigger: The result is displayed after the group member information is changed.
///
/// Related APIs: [setGroupMemberNickname] : updates the nickname of a group member. [setGroupMemberRole] : updates the group member role. [transferGroupOwner], group master transfer.
///
///
/// @param zim ZIM instance.
/// @param operatedInfo Updated member information.
/// @param userInfo userInfo.
/// @param groupID groupID.
- (void)zim:(ZIM *)zim
    groupMemberInfoUpdated:(NSArray<ZIMGroupMemberInfo *> *)userInfo
              operatedInfo:(ZIMGroupOperatedInfo *)operatedInfo
                   groupID:(NSString *)groupID;

/// Supported versions: 2.0.0 and above.
///
/// Detail description: After the inviter initiates a call invitation, the invitee will receive this callback when the invitee is online.
///
/// Business scenario: The invitee will call this callback after the inviter sends a call invitation.
///
/// When to call: After creating a ZIM instance through [create].
///
/// Note: If the user is not in the invitation list or not online, this callback will not be called.
///
/// Related interface: [callInvite].
///
/// @param zim ZIM instance.
/// @param info Information about received call invitations.
/// @param callID Received CallID.
- (void)zim:(ZIM *)zim
    callInvitationReceived:(ZIMCallInvitationReceivedInfo *)info
                    callID:(NSString *)callID;

/// Supported versions: 2.0.0 and above.
///
/// Detail description: After the inviter cancels the call invitation, this callback will be received when the invitee is online.
///
/// Business scenario: The invitee will call this callback after the inviter cancels the call invitation.
///
/// When to call: After creating a ZIM instance through [create].
///
/// Note: If the user is not in the cancel invitation list or is offline, this callback will not be called.
///
/// Related interface: [callCancel].
///
///
/// @param zim ZIM instance.
/// @param info  Information about canceled call invitations.
/// @param callID Cancelled callID.
- (void)zim:(ZIM *)zim
    callInvitationCancelled:(ZIMCallInvitationCancelledInfo *)info
                     callID:(NSString *)callID;

/// Supported versions: 2.0.0 and above.
///
/// Detail description: After the invitee accepts the call invitation, this callback will be received when the inviter is online.
///
/// Business scenario: The inviter will receive this callback after the inviter accepts the call invitation.
///
/// When to call: After creating a ZIM instance through [create].
///
/// Note: This callback will not be called if the user is not online.
///
/// Related interface: [callAccept].
///
///
/// @param zim ZIM instance.
/// @param info  Information about the call invitations.
/// @param callID  callID.
- (void)zim:(ZIM *)zim
    callInvitationAccepted:(ZIMCallInvitationAcceptedInfo *)info
                    callID:(NSString *)callID;

/// Available since: 2.0.0 and above.
///
/// Description: This callback will be received when the inviter is online after the inviter rejects the call invitation.
///
/// Use cases: The inviter will receive this callback after the inviter declines the call invitation.
///
/// Default value: After creating a ZIM instance through [create] and logging in.
///
/// When to call /Trigger: After creating a ZIM instance through [create] and logging in.
///
/// Restrictions: If the user is not the inviter of the call invitation or is not online, the callback will not be received.
///
/// Related APIs:[callReject].
/// @param zim ZIM instance.
/// @param info  Information about the call invitations.
/// @param callID  callID.
- (void)zim:(ZIM *)zim
    callInvitationRejected:(ZIMCallInvitationRejectedInfo *)info
                    callID:(NSString *)callID;

/// Available since: 2.0.0 and above.
///
/// Description: This callback will be received when the inviter is online after the inviter rejects the call invitation.
///
/// Use cases: The inviter will receive this callback after the inviter declines the call invitation.
///
/// Default value: After creating a ZIM instance through [create] and logging in.
///
/// When to call /Trigger: After creating a ZIM instance through [create] and logging in.
///
/// Restrictions: If the user is not the inviter of the call invitation or is not online, the callback will not be received.
///
/// Related APIs:[callReject].
/// @param zim ZIM instance.
/// @param callID  callID.
- (void)zim:(ZIM *)zim callInvitationTimeout:(NSString *)callID;

/// Supported versions: 2.0.0 and above.
///
/// Detail description: When the call invitation times out, the invitee does not respond, and the inviter will receive a callback.
///
/// Business scenario: The invitee does not respond before the timeout period, and the inviter will receive this callback.
///
/// When to call: After creating a ZIM instance through [create].
///
/// Note: If the user is not the inviter who initiated this call invitation or is not online, the callback will not be received.
///
/// Related interfaces: [callInvite], [callAccept], [callReject].
///
/// @param zim ZIM instance.
/// @param invitees  Timeout invitee ID.
/// @param callID callID.
- (void)zim:(ZIM *)zim
    callInviteesAnsweredTimeout:(NSArray<NSString *> *)invitees
                         callID:(NSString *)callID;

@end

NS_ASSUME_NONNULL_END
