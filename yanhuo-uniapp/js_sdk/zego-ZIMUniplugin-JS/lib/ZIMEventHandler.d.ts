import { ZIMConnectionState, ZIMConnectionEvent, ZIMUserInfo, ZIMMessage, ZIMConversationChangeInfo, ZIMRoomState, ZIMRoomEvent, ZIMRoomAttributesUpdateInfo, ZIMGroupMemberInfo, ZIMGroupOperatedInfo, ZIMGroupMemberState, ZIMGroupMemberEvent, ZIMGroupFullInfo, ZIMGroupState, ZIMGroupEvent, ZIMGroupAttributesUpdateInfo, ZIMRoomMemberAttributesUpdateInfo, ZIMRoomOperatedInfo, ZIMMessageReceiptInfo, ZIMRevokeMessage, ZIMMessageSentStatusChangeInfo } from './ZIMDefines';
import { ZIMError } from './ZIMErrorCode';
import ZIM from './ZIM';
export declare type ZegoAnyCallback = (...args: any[]) => any;
export interface ZIMEventHandler {
    error: (zim: ZIM, errorInfo: ZIMError) => void;
    connectionStateChanged: (zim: ZIM, data: ZIMEventOfConnectionStateChangedResult) => void;
    tokenWillExpire: (zim: ZIM, data: ZIMEventOfTokenWillExpireResult) => void;
    conversationChanged: (zim: ZIM, data: ZIMEventOfConversationChangedResult) => void;
    conversationTotalUnreadMessageCountUpdated: (zim: ZIM, data: ZIMEventOfConversationTotalUnreadMessageCountUpdatedResult) => void;
    receivePeerMessage: (zim: ZIM, data: ZIMEventOfReceiveConversationMessageResult) => void;
    receiveGroupMessage: (zim: ZIM, data: ZIMEventOfReceiveConversationMessageResult) => void;
    receiveRoomMessage: (zim: ZIM, data: ZIMEventOfReceiveConversationMessageResult) => void;
    conversationMessageReceiptChanged: (zim: ZIM, data: ZIMEventOfMessageReceiptChangedResult) => void;
    messageReceiptChanged: (zim: ZIM, data: ZIMEventOfMessageReceiptChangedResult) => void;
    messageRevokeReceived: (zim: ZIM, data: ZIMEventOfMessageRevokeReceivedResult) => void;
    messageSentStatusChanged: (zim: ZIM, data: ZIMEventOfMessageSentStatusChangedResult) => void;
    roomStateChanged: (zim: ZIM, data: ZIMEventOfRoomStateChangedResult) => void;
    roomMemberJoined: (zim: ZIM, data: ZIMEventOfRoomMemberChangedResult) => void;
    roomMemberLeft: (zim: ZIM, data: ZIMEventOfRoomMemberChangedResult) => void;
    roomAttributesUpdated: (zim: ZIM, data: ZIMEventOfRoomAttributesUpdatedResult) => void;
    roomAttributesBatchUpdated: (zim: ZIM, data: ZIMEventOfRoomAttributesUpdatedResult) => void;
    roomMemberAttributesUpdated: (zim: ZIM, data: ZIMEventOfRoomMembersAttributesUpdatedResult) => void;
    groupStateChanged: (zim: ZIM, data: ZIMEventOfGroupStateChangedResult) => void;
    groupNameUpdated: (zim: ZIM, data: ZIMEventOfGroupNameUpdatedResult) => void;
    groupAvatarUrlUpdated: (zim: ZIM, data: ZIMEventOfGroupAvatarUrlUpdatedResult) => void;
    groupNoticeUpdated: (zim: ZIM, data: ZIMEventOfGroupNoticeUpdatedResult) => void;
    groupAttributesUpdated: (zim: ZIM, data: ZIMEventOfGroupAttributesUpdatedResult) => void;
    groupMemberStateChanged: (zim: ZIM, data: ZIMEventOfGroupMemberStateChangedResult) => void;
    groupMemberInfoUpdated: (zim: ZIM, data: ZIMEventOfGroupMemberInfoUpdatedResult) => void;
    callInvitationReceived: (zim: ZIM, data: ZIMEventOfCallInvitationReceivedResult) => void;
    callInvitationCancelled: (zim: ZIM, data: ZIMEventOfCallInvitationCancelledResult) => void;
    callInvitationTimeout: (zim: ZIM, data: ZIMEventOfCallInvitationTimeoutResult) => void;
    callInvitationAccepted: (zim: ZIM, data: ZIMEventOfCallInvitationAcceptedResult) => void;
    callInvitationRejected: (zim: ZIM, data: ZIMEventOfCallInvitationRejectedResult) => void;
    callInviteesAnsweredTimeout: (zim: ZIM, data: ZIMEventOfCallInviteesAnsweredTimeoutResult) => void;
}
export interface ZIMEventOfConnectionStateChangedResult {
    state: ZIMConnectionState;
    event: ZIMConnectionEvent;
    extendedData: string;
}
export interface ZIMEventOfTokenWillExpireResult {
    second: number;
}
export interface ZIMEventOfConversationChangedResult {
    infoList: ZIMConversationChangeInfo[];
}
export interface ZIMEventOfConversationTotalUnreadMessageCountUpdatedResult {
    totalUnreadMessageCount: number;
}
export interface ZIMEventOfReceiveConversationMessageResult {
    messageList: ZIMMessage[];
    fromConversationID: string;
}
export interface ZIMEventOfMessageReceiptChangedResult {
    infos: ZIMMessageReceiptInfo[];
}
export interface ZIMEventOfMessageRevokeReceivedResult {
    messageList: ZIMRevokeMessage[];
}
export interface ZIMEventOfRoomStateChangedResult {
    roomID: string;
    state: ZIMRoomState;
    event: ZIMRoomEvent;
    extendedData: string;
}
export interface ZIMEventOfRoomStateChangedResult {
    roomID: string;
    state: ZIMRoomState;
    event: ZIMRoomEvent;
    extendedData: string;
}
export interface ZIMEventOfRoomMemberChangedResult {
    roomID: string;
    memberList: ZIMUserInfo[];
}
export interface ZIMEventOfRoomAttributesUpdatedResult {
    roomID: string;
    infos: ZIMRoomAttributesUpdateInfo[];
}
export interface ZIMEventOfGroupStateChangedResult {
    state: ZIMGroupState;
    event: ZIMGroupEvent;
    operatedInfo: ZIMGroupOperatedInfo;
    groupInfo: ZIMGroupFullInfo;
}
export interface ZIMEventOfGroupNameUpdatedResult {
    groupID: string;
    groupName: string;
    operatedInfo: ZIMGroupOperatedInfo;
}
export interface ZIMEventOfGroupAvatarUrlUpdatedResult {
    groupID: string;
    groupAvatarUrl: string;
    operatedInfo: ZIMGroupOperatedInfo;
}
export interface ZIMEventOfGroupNoticeUpdatedResult {
    groupID: string;
    groupNotice: string;
    operatedInfo: ZIMGroupOperatedInfo;
}
export interface ZIMEventOfGroupAttributesUpdatedResult {
    groupID: string;
    infoList: ZIMGroupAttributesUpdateInfo[];
    operatedInfo: ZIMGroupOperatedInfo;
}
export interface ZIMEventOfGroupMemberStateChangedResult {
    groupID: string;
    state: ZIMGroupMemberState;
    event: ZIMGroupMemberEvent;
    userList: ZIMGroupMemberInfo[];
    operatedInfo: ZIMGroupOperatedInfo;
}
export interface ZIMEventOfGroupMemberInfoUpdatedResult {
    groupID: string;
    userList: ZIMGroupMemberInfo[];
    operatedInfo: ZIMGroupOperatedInfo;
}
export interface ZIMEventOfCallInvitationReceivedResult {
    callID: string;
    inviter: string;
    extendedData: string;
    timeout: number;
}
export interface ZIMEventOfCallInvitationCancelledResult {
    callID: string;
    inviter: string;
    extendedData: string;
}
export interface ZIMEventOfCallInvitationTimeoutResult {
    callID: string;
}
export interface ZIMEventOfCallInvitationAcceptedResult {
    callID: string;
    invitee: string;
    extendedData: string;
}
export interface ZIMEventOfCallInvitationRejectedResult {
    callID: string;
    invitee: string;
    extendedData: string;
}
export interface ZIMEventOfCallInviteesAnsweredTimeoutResult {
    callID: string;
    invitees: string[];
}
export interface ZIMEventOfRoomMembersAttributesUpdatedResult {
    roomID: string;
    infos: ZIMRoomMemberAttributesUpdateInfo[];
    operatedInfo: ZIMRoomOperatedInfo;
}
export interface ZIMEventOfMessageSentStatusChangedResult {
    infos: ZIMMessageSentStatusChangeInfo[];
}
