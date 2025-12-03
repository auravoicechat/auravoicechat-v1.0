import 'package:flutter_creat_module/generated/json/base/json_convert_content.dart';
import 'package:flutter_creat_module/pages/login/model/login_register_update_profile_model_entity.dart';

LoginRegisterUpdateProfileModelEntity
    $LoginRegisterUpdateProfileModelEntityFromJson(Map<String, dynamic> json) {
  final LoginRegisterUpdateProfileModelEntity
      loginRegisterUpdateProfileModelEntity =
      LoginRegisterUpdateProfileModelEntity();
  final LoginRegisterUpdateProfileModelUser? user =
      jsonConvert.convert<LoginRegisterUpdateProfileModelUser>(json['user']);
  if (user != null) {
    loginRegisterUpdateProfileModelEntity.user = user;
  }
  return loginRegisterUpdateProfileModelEntity;
}

Map<String, dynamic> $LoginRegisterUpdateProfileModelEntityToJson(
    LoginRegisterUpdateProfileModelEntity entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['user'] = entity.user?.toJson();
  return data;
}

extension LoginRegisterUpdateProfileModelEntityExtension
    on LoginRegisterUpdateProfileModelEntity {
  LoginRegisterUpdateProfileModelEntity copyWith({
    LoginRegisterUpdateProfileModelUser? user,
  }) {
    return LoginRegisterUpdateProfileModelEntity()..user = user ?? this.user;
  }
}

LoginRegisterUpdateProfileModelUser
    $LoginRegisterUpdateProfileModelUserFromJson(Map<String, dynamic> json) {
  final LoginRegisterUpdateProfileModelUser
      loginRegisterUpdateProfileModelUser =
      LoginRegisterUpdateProfileModelUser();
  final String? uid = jsonConvert.convert<String>(json['uid']);
  if (uid != null) {
    loginRegisterUpdateProfileModelUser.uid = uid;
  }
  final String? userName = jsonConvert.convert<String>(json['userName']);
  if (userName != null) {
    loginRegisterUpdateProfileModelUser.userName = userName;
  }
  final String? website = jsonConvert.convert<String>(json['website']);
  if (website != null) {
    loginRegisterUpdateProfileModelUser.website = website;
  }
  final String? bio = jsonConvert.convert<String>(json['bio']);
  if (bio != null) {
    loginRegisterUpdateProfileModelUser.bio = bio;
  }
  final String? email = jsonConvert.convert<String>(json['email']);
  if (email != null) {
    loginRegisterUpdateProfileModelUser.email = email;
  }
  final String? faceImgUrl = jsonConvert.convert<String>(json['faceImgUrl']);
  if (faceImgUrl != null) {
    loginRegisterUpdateProfileModelUser.faceImgUrl = faceImgUrl;
  }
  final int? userLevel = jsonConvert.convert<int>(json['userLevel']);
  if (userLevel != null) {
    loginRegisterUpdateProfileModelUser.userLevel = userLevel;
  }
  final int? createTime = jsonConvert.convert<int>(json['createTime']);
  if (createTime != null) {
    loginRegisterUpdateProfileModelUser.createTime = createTime;
  }
  final dynamic insNo = json['insNo'];
  if (insNo != null) {
    loginRegisterUpdateProfileModelUser.insNo = insNo;
  }
  final dynamic youtubeNo = json['youtubeNo'];
  if (youtubeNo != null) {
    loginRegisterUpdateProfileModelUser.youtubeNo = youtubeNo;
  }
  final int? sex = jsonConvert.convert<int>(json['sex']);
  if (sex != null) {
    loginRegisterUpdateProfileModelUser.sex = sex;
  }
  final int? funid = jsonConvert.convert<int>(json['funid']);
  if (funid != null) {
    loginRegisterUpdateProfileModelUser.funid = funid;
  }
  final int? birthday = jsonConvert.convert<int>(json['birthday']);
  if (birthday != null) {
    loginRegisterUpdateProfileModelUser.birthday = birthday;
  }
  final dynamic longitude = json['longitude'];
  if (longitude != null) {
    loginRegisterUpdateProfileModelUser.longitude = longitude;
  }
  final dynamic latitude = json['latitude'];
  if (latitude != null) {
    loginRegisterUpdateProfileModelUser.latitude = latitude;
  }
  final int? updateTime = jsonConvert.convert<int>(json['updateTime']);
  if (updateTime != null) {
    loginRegisterUpdateProfileModelUser.updateTime = updateTime;
  }
  final dynamic bgUrl = json['bgUrl'];
  if (bgUrl != null) {
    loginRegisterUpdateProfileModelUser.bgUrl = bgUrl;
  }
  final LoginRegisterUpdateProfileModelUserCountry? country = jsonConvert
      .convert<LoginRegisterUpdateProfileModelUserCountry>(json['country']);
  if (country != null) {
    loginRegisterUpdateProfileModelUser.country = country;
  }
  final LoginRegisterUpdateProfileModelUserExt? ext =
      jsonConvert.convert<LoginRegisterUpdateProfileModelUserExt>(json['ext']);
  if (ext != null) {
    loginRegisterUpdateProfileModelUser.ext = ext;
  }
  return loginRegisterUpdateProfileModelUser;
}

Map<String, dynamic> $LoginRegisterUpdateProfileModelUserToJson(
    LoginRegisterUpdateProfileModelUser entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['uid'] = entity.uid;
  data['userName'] = entity.userName;
  data['website'] = entity.website;
  data['bio'] = entity.bio;
  data['email'] = entity.email;
  data['faceImgUrl'] = entity.faceImgUrl;
  data['userLevel'] = entity.userLevel;
  data['createTime'] = entity.createTime;
  data['insNo'] = entity.insNo;
  data['youtubeNo'] = entity.youtubeNo;
  data['sex'] = entity.sex;
  data['funid'] = entity.funid;
  data['birthday'] = entity.birthday;
  data['longitude'] = entity.longitude;
  data['latitude'] = entity.latitude;
  data['updateTime'] = entity.updateTime;
  data['bgUrl'] = entity.bgUrl;
  data['country'] = entity.country?.toJson();
  data['ext'] = entity.ext?.toJson();
  return data;
}

extension LoginRegisterUpdateProfileModelUserExtension
    on LoginRegisterUpdateProfileModelUser {
  LoginRegisterUpdateProfileModelUser copyWith({
    String? uid,
    String? userName,
    String? website,
    String? bio,
    String? email,
    String? faceImgUrl,
    int? userLevel,
    int? createTime,
    dynamic insNo,
    dynamic youtubeNo,
    int? sex,
    int? funid,
    int? birthday,
    dynamic longitude,
    dynamic latitude,
    int? updateTime,
    dynamic bgUrl,
    LoginRegisterUpdateProfileModelUserCountry? country,
    LoginRegisterUpdateProfileModelUserExt? ext,
  }) {
    return LoginRegisterUpdateProfileModelUser()
      ..uid = uid ?? this.uid
      ..userName = userName ?? this.userName
      ..website = website ?? this.website
      ..bio = bio ?? this.bio
      ..email = email ?? this.email
      ..faceImgUrl = faceImgUrl ?? this.faceImgUrl
      ..userLevel = userLevel ?? this.userLevel
      ..createTime = createTime ?? this.createTime
      ..insNo = insNo ?? this.insNo
      ..youtubeNo = youtubeNo ?? this.youtubeNo
      ..sex = sex ?? this.sex
      ..funid = funid ?? this.funid
      ..birthday = birthday ?? this.birthday
      ..longitude = longitude ?? this.longitude
      ..latitude = latitude ?? this.latitude
      ..updateTime = updateTime ?? this.updateTime
      ..bgUrl = bgUrl ?? this.bgUrl
      ..country = country ?? this.country
      ..ext = ext ?? this.ext;
  }
}

LoginRegisterUpdateProfileModelUserCountry
    $LoginRegisterUpdateProfileModelUserCountryFromJson(
        Map<String, dynamic> json) {
  final LoginRegisterUpdateProfileModelUserCountry
      loginRegisterUpdateProfileModelUserCountry =
      LoginRegisterUpdateProfileModelUserCountry();
  final String? countryCode = jsonConvert.convert<String>(json['countryCode']);
  if (countryCode != null) {
    loginRegisterUpdateProfileModelUserCountry.countryCode = countryCode;
  }
  final dynamic icon = json['icon'];
  if (icon != null) {
    loginRegisterUpdateProfileModelUserCountry.icon = icon;
  }
  final String? countryName = jsonConvert.convert<String>(json['countryName']);
  if (countryName != null) {
    loginRegisterUpdateProfileModelUserCountry.countryName = countryName;
  }
  return loginRegisterUpdateProfileModelUserCountry;
}

Map<String, dynamic> $LoginRegisterUpdateProfileModelUserCountryToJson(
    LoginRegisterUpdateProfileModelUserCountry entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['countryCode'] = entity.countryCode;
  data['icon'] = entity.icon;
  data['countryName'] = entity.countryName;
  return data;
}

extension LoginRegisterUpdateProfileModelUserCountryExtension
    on LoginRegisterUpdateProfileModelUserCountry {
  LoginRegisterUpdateProfileModelUserCountry copyWith({
    String? countryCode,
    dynamic icon,
    String? countryName,
  }) {
    return LoginRegisterUpdateProfileModelUserCountry()
      ..countryCode = countryCode ?? this.countryCode
      ..icon = icon ?? this.icon
      ..countryName = countryName ?? this.countryName;
  }
}

LoginRegisterUpdateProfileModelUserExt
    $LoginRegisterUpdateProfileModelUserExtFromJson(Map<String, dynamic> json) {
  final LoginRegisterUpdateProfileModelUserExt
      loginRegisterUpdateProfileModelUserExt =
      LoginRegisterUpdateProfileModelUserExt();
  final String? phone = jsonConvert.convert<String>(json['phone']);
  if (phone != null) {
    loginRegisterUpdateProfileModelUserExt.phone = phone;
  }
  final String? phoneCode = jsonConvert.convert<String>(json['phoneCode']);
  if (phoneCode != null) {
    loginRegisterUpdateProfileModelUserExt.phoneCode = phoneCode;
  }
  final int? loginType = jsonConvert.convert<int>(json['loginType']);
  if (loginType != null) {
    loginRegisterUpdateProfileModelUserExt.loginType = loginType;
  }
  final dynamic funUserType = json['funUserType'];
  if (funUserType != null) {
    loginRegisterUpdateProfileModelUserExt.funUserType = funUserType;
  }
  final bool? bindFacebook = jsonConvert.convert<bool>(json['bindFacebook']);
  if (bindFacebook != null) {
    loginRegisterUpdateProfileModelUserExt.bindFacebook = bindFacebook;
  }
  final bool? bindGoogle = jsonConvert.convert<bool>(json['bindGoogle']);
  if (bindGoogle != null) {
    loginRegisterUpdateProfileModelUserExt.bindGoogle = bindGoogle;
  }
  final bool? bindApple = jsonConvert.convert<bool>(json['bindApple']);
  if (bindApple != null) {
    loginRegisterUpdateProfileModelUserExt.bindApple = bindApple;
  }
  final bool? bindTwitter = jsonConvert.convert<bool>(json['bindTwitter']);
  if (bindTwitter != null) {
    loginRegisterUpdateProfileModelUserExt.bindTwitter = bindTwitter;
  }
  final String? countryCode = jsonConvert.convert<String>(json['countryCode']);
  if (countryCode != null) {
    loginRegisterUpdateProfileModelUserExt.countryCode = countryCode;
  }
  final int? region = jsonConvert.convert<int>(json['region']);
  if (region != null) {
    loginRegisterUpdateProfileModelUserExt.region = region;
  }
  final String? languageType =
      jsonConvert.convert<String>(json['languageType']);
  if (languageType != null) {
    loginRegisterUpdateProfileModelUserExt.languageType = languageType;
  }
  final dynamic thirdEmail = json['thirdEmail'];
  if (thirdEmail != null) {
    loginRegisterUpdateProfileModelUserExt.thirdEmail = thirdEmail;
  }
  final dynamic charmId = json['charmId'];
  if (charmId != null) {
    loginRegisterUpdateProfileModelUserExt.charmId = charmId;
  }
  final dynamic charmIdExpiredTime = json['charmIdExpiredTime'];
  if (charmIdExpiredTime != null) {
    loginRegisterUpdateProfileModelUserExt.charmIdExpiredTime =
        charmIdExpiredTime;
  }
  final dynamic sid = json['sid'];
  if (sid != null) {
    loginRegisterUpdateProfileModelUserExt.sid = sid;
  }
  final String? howToChooseCountry =
      jsonConvert.convert<String>(json['howToChooseCountry']);
  if (howToChooseCountry != null) {
    loginRegisterUpdateProfileModelUserExt.howToChooseCountry =
        howToChooseCountry;
  }
  final dynamic userCancelStatus = json['userCancelStatus'];
  if (userCancelStatus != null) {
    loginRegisterUpdateProfileModelUserExt.userCancelStatus = userCancelStatus;
  }
  final dynamic userCancelEndDate = json['userCancelEndDate'];
  if (userCancelEndDate != null) {
    loginRegisterUpdateProfileModelUserExt.userCancelEndDate =
        userCancelEndDate;
  }
  final int? tabIndex = jsonConvert.convert<int>(json['tabIndex']);
  if (tabIndex != null) {
    loginRegisterUpdateProfileModelUserExt.tabIndex = tabIndex;
  }
  final int? changeCountryCount =
      jsonConvert.convert<int>(json['changeCountryCount']);
  if (changeCountryCount != null) {
    loginRegisterUpdateProfileModelUserExt.changeCountryCount =
        changeCountryCount;
  }
  final String? ip = jsonConvert.convert<String>(json['ip']);
  if (ip != null) {
    loginRegisterUpdateProfileModelUserExt.ip = ip;
  }
  final bool? showRoomSupport =
      jsonConvert.convert<bool>(json['showRoomSupport']);
  if (showRoomSupport != null) {
    loginRegisterUpdateProfileModelUserExt.showRoomSupport = showRoomSupport;
  }
  return loginRegisterUpdateProfileModelUserExt;
}

Map<String, dynamic> $LoginRegisterUpdateProfileModelUserExtToJson(
    LoginRegisterUpdateProfileModelUserExt entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['phone'] = entity.phone;
  data['phoneCode'] = entity.phoneCode;
  data['loginType'] = entity.loginType;
  data['funUserType'] = entity.funUserType;
  data['bindFacebook'] = entity.bindFacebook;
  data['bindGoogle'] = entity.bindGoogle;
  data['bindApple'] = entity.bindApple;
  data['bindTwitter'] = entity.bindTwitter;
  data['countryCode'] = entity.countryCode;
  data['region'] = entity.region;
  data['languageType'] = entity.languageType;
  data['thirdEmail'] = entity.thirdEmail;
  data['charmId'] = entity.charmId;
  data['charmIdExpiredTime'] = entity.charmIdExpiredTime;
  data['sid'] = entity.sid;
  data['howToChooseCountry'] = entity.howToChooseCountry;
  data['userCancelStatus'] = entity.userCancelStatus;
  data['userCancelEndDate'] = entity.userCancelEndDate;
  data['tabIndex'] = entity.tabIndex;
  data['changeCountryCount'] = entity.changeCountryCount;
  data['ip'] = entity.ip;
  data['showRoomSupport'] = entity.showRoomSupport;
  return data;
}

extension LoginRegisterUpdateProfileModelUserExtExtension
    on LoginRegisterUpdateProfileModelUserExt {
  LoginRegisterUpdateProfileModelUserExt copyWith({
    String? phone,
    String? phoneCode,
    int? loginType,
    dynamic funUserType,
    bool? bindFacebook,
    bool? bindGoogle,
    bool? bindApple,
    bool? bindTwitter,
    String? countryCode,
    int? region,
    String? languageType,
    dynamic thirdEmail,
    dynamic charmId,
    dynamic charmIdExpiredTime,
    dynamic sid,
    String? howToChooseCountry,
    dynamic userCancelStatus,
    dynamic userCancelEndDate,
    int? tabIndex,
    int? changeCountryCount,
    String? ip,
    bool? showRoomSupport,
  }) {
    return LoginRegisterUpdateProfileModelUserExt()
      ..phone = phone ?? this.phone
      ..phoneCode = phoneCode ?? this.phoneCode
      ..loginType = loginType ?? this.loginType
      ..funUserType = funUserType ?? this.funUserType
      ..bindFacebook = bindFacebook ?? this.bindFacebook
      ..bindGoogle = bindGoogle ?? this.bindGoogle
      ..bindApple = bindApple ?? this.bindApple
      ..bindTwitter = bindTwitter ?? this.bindTwitter
      ..countryCode = countryCode ?? this.countryCode
      ..region = region ?? this.region
      ..languageType = languageType ?? this.languageType
      ..thirdEmail = thirdEmail ?? this.thirdEmail
      ..charmId = charmId ?? this.charmId
      ..charmIdExpiredTime = charmIdExpiredTime ?? this.charmIdExpiredTime
      ..sid = sid ?? this.sid
      ..howToChooseCountry = howToChooseCountry ?? this.howToChooseCountry
      ..userCancelStatus = userCancelStatus ?? this.userCancelStatus
      ..userCancelEndDate = userCancelEndDate ?? this.userCancelEndDate
      ..tabIndex = tabIndex ?? this.tabIndex
      ..changeCountryCount = changeCountryCount ?? this.changeCountryCount
      ..ip = ip ?? this.ip
      ..showRoomSupport = showRoomSupport ?? this.showRoomSupport;
  }
}
