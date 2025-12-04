import 'package:flutter_creat_module/generated/json/base/json_convert_content.dart';
import 'package:flutter_creat_module/commom/model/profile_user_pro_model_entity.dart';

ProfileUserProModelEntity $ProfileUserProModelEntityFromJson(
    Map<String, dynamic> json) {
  final ProfileUserProModelEntity profileUserProModelEntity =
      ProfileUserProModelEntity();
  final String? sessionId = jsonConvert.convert<String>(json['sessionId']);
  if (sessionId != null) {
    profileUserProModelEntity.sessionId = sessionId;
  }
  final int? listType = jsonConvert.convert<int>(json['listType']);
  if (listType != null) {
    profileUserProModelEntity.listType = listType;
  }
  final String? requestType = jsonConvert.convert<String>(json['requestType']);
  if (requestType != null) {
    profileUserProModelEntity.requestType = requestType;
  }
  final List<ProfileUserProModelBanner>? banner =
      (json['banner'] as List<dynamic>?)
          ?.map((e) => jsonConvert.convert<ProfileUserProModelBanner>(e)
              as ProfileUserProModelBanner)
          .toList();
  if (banner != null) {
    profileUserProModelEntity.banner = banner;
  }
  final List<ProfileUserProModelBtns>? btns = (json['btns'] as List<dynamic>?)
      ?.map((e) => jsonConvert.convert<ProfileUserProModelBtns>(e)
          as ProfileUserProModelBtns)
      .toList();
  if (btns != null) {
    profileUserProModelEntity.btns = btns;
  }
  final List<ProfileUserProModelList>? list = (json['list'] as List<dynamic>?)
      ?.map((e) => jsonConvert.convert<ProfileUserProModelList>(e)
          as ProfileUserProModelList)
      .toList();
  if (list != null) {
    profileUserProModelEntity.list = list;
  }
  final bool? more = jsonConvert.convert<bool>(json['more']);
  if (more != null) {
    profileUserProModelEntity.more = more;
  }
  final bool? haveRoomRole = jsonConvert.convert<bool>(json['haveRoomRole']);
  if (haveRoomRole != null) {
    profileUserProModelEntity.haveRoomRole = haveRoomRole;
  }
  return profileUserProModelEntity;
}

Map<String, dynamic> $ProfileUserProModelEntityToJson(
    ProfileUserProModelEntity entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['sessionId'] = entity.sessionId;
  data['listType'] = entity.listType;
  data['requestType'] = entity.requestType;
  data['banner'] = entity.banner?.map((v) => v.toJson()).toList();
  data['btns'] = entity.btns?.map((v) => v.toJson()).toList();
  data['list'] = entity.list?.map((v) => v.toJson()).toList();
  data['more'] = entity.more;
  data['haveRoomRole'] = entity.haveRoomRole;
  return data;
}

extension ProfileUserProModelEntityExtension on ProfileUserProModelEntity {
  ProfileUserProModelEntity copyWith({
    String? sessionId,
    int? listType,
    String? requestType,
    List<ProfileUserProModelBanner>? banner,
    List<ProfileUserProModelBtns>? btns,
    List<ProfileUserProModelList>? list,
    bool? more,
    bool? haveRoomRole,
  }) {
    return ProfileUserProModelEntity()
      ..sessionId = sessionId ?? this.sessionId
      ..listType = listType ?? this.listType
      ..requestType = requestType ?? this.requestType
      ..banner = banner ?? this.banner
      ..btns = btns ?? this.btns
      ..list = list ?? this.list
      ..more = more ?? this.more
      ..haveRoomRole = haveRoomRole ?? this.haveRoomRole;
  }
}

ProfileUserProModelBanner $ProfileUserProModelBannerFromJson(
    Map<String, dynamic> json) {
  final ProfileUserProModelBanner profileUserProModelBanner =
      ProfileUserProModelBanner();
  final int? id = jsonConvert.convert<int>(json['id']);
  if (id != null) {
    profileUserProModelBanner.id = id;
  }
  final String? imgUrl = jsonConvert.convert<String>(json['imgUrl']);
  if (imgUrl != null) {
    profileUserProModelBanner.imgUrl = imgUrl;
  }
  final String? action = jsonConvert.convert<String>(json['action']);
  if (action != null) {
    profileUserProModelBanner.action = action;
  }
  final String? usage = jsonConvert.convert<String>(json['usage']);
  if (usage != null) {
    profileUserProModelBanner.usage = usage;
  }
  final String? type = jsonConvert.convert<String>(json['type']);
  if (type != null) {
    profileUserProModelBanner.type = type;
  }
  final int? startTime = jsonConvert.convert<int>(json['startTime']);
  if (startTime != null) {
    profileUserProModelBanner.startTime = startTime;
  }
  final int? endTime = jsonConvert.convert<int>(json['endTime']);
  if (endTime != null) {
    profileUserProModelBanner.endTime = endTime;
  }
  final int? state = jsonConvert.convert<int>(json['state']);
  if (state != null) {
    profileUserProModelBanner.state = state;
  }
  final double? index = jsonConvert.convert<double>(json['index']);
  if (index != null) {
    profileUserProModelBanner.index = index;
  }
  final String? areaType = jsonConvert.convert<String>(json['areaType']);
  if (areaType != null) {
    profileUserProModelBanner.areaType = areaType;
  }
  final String? roomBannerType =
      jsonConvert.convert<String>(json['roomBannerType']);
  if (roomBannerType != null) {
    profileUserProModelBanner.roomBannerType = roomBannerType;
  }
  final String? defaultAction =
      jsonConvert.convert<String>(json['defaultAction']);
  if (defaultAction != null) {
    profileUserProModelBanner.defaultAction = defaultAction;
  }
  return profileUserProModelBanner;
}

Map<String, dynamic> $ProfileUserProModelBannerToJson(
    ProfileUserProModelBanner entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['id'] = entity.id;
  data['imgUrl'] = entity.imgUrl;
  data['action'] = entity.action;
  data['usage'] = entity.usage;
  data['type'] = entity.type;
  data['startTime'] = entity.startTime;
  data['endTime'] = entity.endTime;
  data['state'] = entity.state;
  data['index'] = entity.index;
  data['areaType'] = entity.areaType;
  data['roomBannerType'] = entity.roomBannerType;
  data['defaultAction'] = entity.defaultAction;
  return data;
}

extension ProfileUserProModelBannerExtension on ProfileUserProModelBanner {
  ProfileUserProModelBanner copyWith({
    int? id,
    String? imgUrl,
    String? action,
    String? usage,
    String? type,
    int? startTime,
    int? endTime,
    int? state,
    double? index,
    String? areaType,
    String? roomBannerType,
    String? defaultAction,
  }) {
    return ProfileUserProModelBanner()
      ..id = id ?? this.id
      ..imgUrl = imgUrl ?? this.imgUrl
      ..action = action ?? this.action
      ..usage = usage ?? this.usage
      ..type = type ?? this.type
      ..startTime = startTime ?? this.startTime
      ..endTime = endTime ?? this.endTime
      ..state = state ?? this.state
      ..index = index ?? this.index
      ..areaType = areaType ?? this.areaType
      ..roomBannerType = roomBannerType ?? this.roomBannerType
      ..defaultAction = defaultAction ?? this.defaultAction;
  }
}

ProfileUserProModelBtns $ProfileUserProModelBtnsFromJson(
    Map<String, dynamic> json) {
  final ProfileUserProModelBtns profileUserProModelBtns =
      ProfileUserProModelBtns();
  final String? id = jsonConvert.convert<String>(json['id']);
  if (id != null) {
    profileUserProModelBtns.id = id;
  }
  final String? name = jsonConvert.convert<String>(json['name']);
  if (name != null) {
    profileUserProModelBtns.name = name;
  }
  final String? icon = jsonConvert.convert<String>(json['icon']);
  if (icon != null) {
    profileUserProModelBtns.icon = icon;
  }
  final String? action = jsonConvert.convert<String>(json['action']);
  if (action != null) {
    profileUserProModelBtns.action = action;
  }
  return profileUserProModelBtns;
}

Map<String, dynamic> $ProfileUserProModelBtnsToJson(
    ProfileUserProModelBtns entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['id'] = entity.id;
  data['name'] = entity.name;
  data['icon'] = entity.icon;
  data['action'] = entity.action;
  return data;
}

extension ProfileUserProModelBtnsExtension on ProfileUserProModelBtns {
  ProfileUserProModelBtns copyWith({
    String? id,
    String? name,
    String? icon,
    String? action,
  }) {
    return ProfileUserProModelBtns()
      ..id = id ?? this.id
      ..name = name ?? this.name
      ..icon = icon ?? this.icon
      ..action = action ?? this.action;
  }
}

ProfileUserProModelList $ProfileUserProModelListFromJson(
    Map<String, dynamic> json) {
  final ProfileUserProModelList profileUserProModelList =
      ProfileUserProModelList();
  final String? id = jsonConvert.convert<String>(json['id']);
  if (id != null) {
    profileUserProModelList.id = id;
  }
  final String? sessionId = jsonConvert.convert<String>(json['sessionId']);
  if (sessionId != null) {
    profileUserProModelList.sessionId = sessionId;
  }
  final int? listType = jsonConvert.convert<int>(json['listType']);
  if (listType != null) {
    profileUserProModelList.listType = listType;
  }
  final String? name = jsonConvert.convert<String>(json['name']);
  if (name != null) {
    profileUserProModelList.name = name;
  }
  final String? announcementContent =
      jsonConvert.convert<String>(json['announcementContent']);
  if (announcementContent != null) {
    profileUserProModelList.announcementContent = announcementContent;
  }
  final String? posterUrl = jsonConvert.convert<String>(json['posterUrl']);
  if (posterUrl != null) {
    profileUserProModelList.posterUrl = posterUrl;
  }
  final int? onlineUserCount =
      jsonConvert.convert<int>(json['onlineUserCount']);
  if (onlineUserCount != null) {
    profileUserProModelList.onlineUserCount = onlineUserCount;
  }
  final int? roomType = jsonConvert.convert<int>(json['roomType']);
  if (roomType != null) {
    profileUserProModelList.roomType = roomType;
  }
  final int? acStatus = jsonConvert.convert<int>(json['acStatus']);
  if (acStatus != null) {
    profileUserProModelList.acStatus = acStatus;
  }
  final int? acBeginDate = jsonConvert.convert<int>(json['acBeginDate']);
  if (acBeginDate != null) {
    profileUserProModelList.acBeginDate = acBeginDate;
  }
  final int? roomGameMode = jsonConvert.convert<int>(json['roomGameMode']);
  if (roomGameMode != null) {
    profileUserProModelList.roomGameMode = roomGameMode;
  }
  final String? countryUrl = jsonConvert.convert<String>(json['countryUrl']);
  if (countryUrl != null) {
    profileUserProModelList.countryUrl = countryUrl;
  }
  final String? countryCode = jsonConvert.convert<String>(json['countryCode']);
  if (countryCode != null) {
    profileUserProModelList.countryCode = countryCode;
  }
  final bool? showTreasures = jsonConvert.convert<bool>(json['showTreasures']);
  if (showTreasures != null) {
    profileUserProModelList.showTreasures = showTreasures;
  }
  final int? charmId = jsonConvert.convert<int>(json['charmId']);
  if (charmId != null) {
    profileUserProModelList.charmId = charmId;
  }
  final String? treasureIcon =
      jsonConvert.convert<String>(json['treasureIcon']);
  if (treasureIcon != null) {
    profileUserProModelList.treasureIcon = treasureIcon;
  }
  return profileUserProModelList;
}

Map<String, dynamic> $ProfileUserProModelListToJson(
    ProfileUserProModelList entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['id'] = entity.id;
  data['sessionId'] = entity.sessionId;
  data['listType'] = entity.listType;
  data['name'] = entity.name;
  data['announcementContent'] = entity.announcementContent;
  data['posterUrl'] = entity.posterUrl;
  data['onlineUserCount'] = entity.onlineUserCount;
  data['roomType'] = entity.roomType;
  data['acStatus'] = entity.acStatus;
  data['acBeginDate'] = entity.acBeginDate;
  data['roomGameMode'] = entity.roomGameMode;
  data['countryUrl'] = entity.countryUrl;
  data['countryCode'] = entity.countryCode;
  data['showTreasures'] = entity.showTreasures;
  data['charmId'] = entity.charmId;
  data['treasureIcon'] = entity.treasureIcon;
  return data;
}

extension ProfileUserProModelListExtension on ProfileUserProModelList {
  ProfileUserProModelList copyWith({
    String? id,
    String? sessionId,
    int? listType,
    String? name,
    String? announcementContent,
    String? posterUrl,
    int? onlineUserCount,
    int? roomType,
    int? acStatus,
    int? acBeginDate,
    int? roomGameMode,
    String? countryUrl,
    String? countryCode,
    bool? showTreasures,
    int? charmId,
    String? treasureIcon,
  }) {
    return ProfileUserProModelList()
      ..id = id ?? this.id
      ..sessionId = sessionId ?? this.sessionId
      ..listType = listType ?? this.listType
      ..name = name ?? this.name
      ..announcementContent = announcementContent ?? this.announcementContent
      ..posterUrl = posterUrl ?? this.posterUrl
      ..onlineUserCount = onlineUserCount ?? this.onlineUserCount
      ..roomType = roomType ?? this.roomType
      ..acStatus = acStatus ?? this.acStatus
      ..acBeginDate = acBeginDate ?? this.acBeginDate
      ..roomGameMode = roomGameMode ?? this.roomGameMode
      ..countryUrl = countryUrl ?? this.countryUrl
      ..countryCode = countryCode ?? this.countryCode
      ..showTreasures = showTreasures ?? this.showTreasures
      ..charmId = charmId ?? this.charmId
      ..treasureIcon = treasureIcon ?? this.treasureIcon;
  }
}
