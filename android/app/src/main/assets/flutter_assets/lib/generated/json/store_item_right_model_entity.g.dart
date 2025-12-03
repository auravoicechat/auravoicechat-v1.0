import 'package:flutter_creat_module/generated/json/base/json_convert_content.dart';
import 'package:flutter_creat_module/pages/me/model/store_item_right_model_entity.dart';
import 'package:flutter_creat_module/pages/me/model/me_friend_items_entity.dart';

import 'package:flutter_creat_module/pages/me/model/me_tab_properties_entity.dart';

StoreItemRightModelEntity $StoreItemRightModelEntityFromJson(
    Map<String, dynamic> json) {
  final StoreItemRightModelEntity storeItemRightModelEntity =
      StoreItemRightModelEntity();
  final List<StoreItemRightModelItems>? items =
      (json['items'] as List<dynamic>?)
          ?.map((e) => jsonConvert.convert<StoreItemRightModelItems>(e)
              as StoreItemRightModelItems)
          .toList();
  if (items != null) {
    storeItemRightModelEntity.items = items;
  }
  final int? type = jsonConvert.convert<int>(json['type']);
  if (type != null) {
    storeItemRightModelEntity.type = type;
  }
  final bool? isLoading = jsonConvert.convert<bool>(json['isLoading']);
  if (isLoading != null) {
    storeItemRightModelEntity.isLoading = isLoading;
  }
  return storeItemRightModelEntity;
}

Map<String, dynamic> $StoreItemRightModelEntityToJson(
    StoreItemRightModelEntity entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['items'] = entity.items?.map((v) => v.toJson()).toList();
  data['type'] = entity.type;
  data['isLoading'] = entity.isLoading;
  return data;
}

extension StoreItemRightModelEntityExtension on StoreItemRightModelEntity {
  StoreItemRightModelEntity copyWith({
    List<StoreItemRightModelItems>? items,
    int? type,
    bool? isLoading,
  }) {
    return StoreItemRightModelEntity()
      ..items = items ?? this.items
      ..type = type ?? this.type
      ..isLoading = isLoading ?? this.isLoading;
  }
}

StoreItemRightModelItems $StoreItemRightModelItemsFromJson(
    Map<String, dynamic> json) {
  final StoreItemRightModelItems storeItemRightModelItems =
      StoreItemRightModelItems();
  final String? id = jsonConvert.convert<String>(json['id']);
  if (id != null) {
    storeItemRightModelItems.id = id;
  }
  final int? type = jsonConvert.convert<int>(json['type']);
  if (type != null) {
    storeItemRightModelItems.type = type;
  }
  final int? needAsset = jsonConvert.convert<int>(json['needAsset']);
  if (needAsset != null) {
    storeItemRightModelItems.needAsset = needAsset;
  }
  final int? price = jsonConvert.convert<int>(json['price']);
  if (price != null) {
    storeItemRightModelItems.price = price;
  }
  final String? name = jsonConvert.convert<String>(json['name']);
  if (name != null) {
    storeItemRightModelItems.name = name;
  }
  final String? icon = jsonConvert.convert<String>(json['icon']);
  if (icon != null) {
    storeItemRightModelItems.icon = icon;
  }
  final String? resUrl = jsonConvert.convert<String>(json['resUrl']);
  if (resUrl != null) {
    storeItemRightModelItems.resUrl = resUrl;
  }
  final String? expire = jsonConvert.convert<String>(json['expire']);
  if (expire != null) {
    storeItemRightModelItems.expire = expire;
  }
  final int? expireDays = jsonConvert.convert<int>(json['expireDays']);
  if (expireDays != null) {
    storeItemRightModelItems.expireDays = expireDays;
  }
  final List<StoreItemRightModelItemsTimePrices>? timePrices =
      (json['timePrices'] as List<dynamic>?)
          ?.map((e) =>
              jsonConvert.convert<StoreItemRightModelItemsTimePrices>(e)
                  as StoreItemRightModelItemsTimePrices)
          .toList();
  if (timePrices != null) {
    storeItemRightModelItems.timePrices = timePrices;
  }
  final String? tips = jsonConvert.convert<String>(json['tips']);
  if (tips != null) {
    storeItemRightModelItems.tips = tips;
  }
  final String? desc = jsonConvert.convert<String>(json['desc']);
  if (desc != null) {
    storeItemRightModelItems.desc = desc;
  }
  final String? title = jsonConvert.convert<String>(json['title']);
  if (title != null) {
    storeItemRightModelItems.title = title;
  }
  final int? update = jsonConvert.convert<int>(json['update']);
  if (update != null) {
    storeItemRightModelItems.update = update;
  }
  final int? rarity = jsonConvert.convert<int>(json['rarity']);
  if (rarity != null) {
    storeItemRightModelItems.rarity = rarity;
  }
  final int? privilege = jsonConvert.convert<int>(json['privilege']);
  if (privilege != null) {
    storeItemRightModelItems.privilege = privilege;
  }
  final int? reviewStatus = jsonConvert.convert<int>(json['reviewStatus']);
  if (reviewStatus != null) {
    storeItemRightModelItems.reviewStatus = reviewStatus;
  }
  final int? state = jsonConvert.convert<int>(json['state']);
  if (state != null) {
    storeItemRightModelItems.state = state;
  }
  final String? privilegeName =
      jsonConvert.convert<String>(json['privilegeName']);
  if (privilegeName != null) {
    storeItemRightModelItems.privilegeName = privilegeName;
  }
  final String? privilegeNameColor =
      jsonConvert.convert<String>(json['privilegeNameColor']);
  if (privilegeNameColor != null) {
    storeItemRightModelItems.privilegeNameColor = privilegeNameColor;
  }
  final int? customIdLen = jsonConvert.convert<int>(json['customIdLen']);
  if (customIdLen != null) {
    storeItemRightModelItems.customIdLen = customIdLen;
  }
  final String? friendUid = jsonConvert.convert<String>(json['friendUid']);
  if (friendUid != null) {
    storeItemRightModelItems.friendUid = friendUid;
  }
  final List<MeTabPropertiesProperties>? properties =
      (json['properties'] as List<dynamic>?)
          ?.map((e) => jsonConvert.convert<MeTabPropertiesProperties>(e)
              as MeTabPropertiesProperties)
          .toList();
  if (properties != null) {
    storeItemRightModelItems.properties = properties;
  }
  final StoreItemRightBottomStateEntity? bottomState =
      jsonConvert.convert<StoreItemRightBottomStateEntity>(json['bottomState']);
  if (bottomState != null) {
    storeItemRightModelItems.bottomState = bottomState;
  }
  final List<MeFriendItemsFriendItems>? friendItems =
      (json['friendItems'] as List<dynamic>?)
          ?.map((e) => jsonConvert.convert<MeFriendItemsFriendItems>(e)
              as MeFriendItemsFriendItems)
          .toList();
  if (friendItems != null) {
    storeItemRightModelItems.friendItems = friendItems;
  }
  return storeItemRightModelItems;
}

Map<String, dynamic> $StoreItemRightModelItemsToJson(
    StoreItemRightModelItems entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['id'] = entity.id;
  data['type'] = entity.type;
  data['needAsset'] = entity.needAsset;
  data['price'] = entity.price;
  data['name'] = entity.name;
  data['icon'] = entity.icon;
  data['resUrl'] = entity.resUrl;
  data['expire'] = entity.expire;
  data['expireDays'] = entity.expireDays;
  data['timePrices'] = entity.timePrices?.map((v) => v.toJson()).toList();
  data['tips'] = entity.tips;
  data['desc'] = entity.desc;
  data['title'] = entity.title;
  data['update'] = entity.update;
  data['rarity'] = entity.rarity;
  data['privilege'] = entity.privilege;
  data['reviewStatus'] = entity.reviewStatus;
  data['state'] = entity.state;
  data['privilegeName'] = entity.privilegeName;
  data['privilegeNameColor'] = entity.privilegeNameColor;
  data['customIdLen'] = entity.customIdLen;
  data['friendUid'] = entity.friendUid;
  data['properties'] = entity.properties?.map((v) => v.toJson()).toList();
  data['bottomState'] = entity.bottomState.toJson();
  data['friendItems'] = entity.friendItems?.map((v) => v.toJson()).toList();
  return data;
}

extension StoreItemRightModelItemsExtension on StoreItemRightModelItems {
  StoreItemRightModelItems copyWith({
    String? id,
    int? type,
    int? needAsset,
    int? price,
    String? name,
    String? icon,
    String? resUrl,
    String? expire,
    int? expireDays,
    List<StoreItemRightModelItemsTimePrices>? timePrices,
    String? tips,
    String? desc,
    String? title,
    int? update,
    int? rarity,
    int? privilege,
    int? reviewStatus,
    int? state,
    String? privilegeName,
    String? privilegeNameColor,
    int? customIdLen,
    String? friendUid,
    List<MeTabPropertiesProperties>? properties,
    StoreItemRightBottomStateEntity? bottomState,
    List<MeFriendItemsFriendItems>? friendItems,
  }) {
    return StoreItemRightModelItems()
      ..id = id ?? this.id
      ..type = type ?? this.type
      ..needAsset = needAsset ?? this.needAsset
      ..price = price ?? this.price
      ..name = name ?? this.name
      ..icon = icon ?? this.icon
      ..resUrl = resUrl ?? this.resUrl
      ..expire = expire ?? this.expire
      ..expireDays = expireDays ?? this.expireDays
      ..timePrices = timePrices ?? this.timePrices
      ..tips = tips ?? this.tips
      ..desc = desc ?? this.desc
      ..title = title ?? this.title
      ..update = update ?? this.update
      ..rarity = rarity ?? this.rarity
      ..privilege = privilege ?? this.privilege
      ..reviewStatus = reviewStatus ?? this.reviewStatus
      ..state = state ?? this.state
      ..privilegeName = privilegeName ?? this.privilegeName
      ..privilegeNameColor = privilegeNameColor ?? this.privilegeNameColor
      ..customIdLen = customIdLen ?? this.customIdLen
      ..friendUid = friendUid ?? this.friendUid
      ..properties = properties ?? this.properties
      ..bottomState = bottomState ?? this.bottomState
      ..friendItems = friendItems ?? this.friendItems;
  }
}

StoreItemRightModelItemsTimePrices $StoreItemRightModelItemsTimePricesFromJson(
    Map<String, dynamic> json) {
  final StoreItemRightModelItemsTimePrices storeItemRightModelItemsTimePrices =
      StoreItemRightModelItemsTimePrices();
  final int? days = jsonConvert.convert<int>(json['days']);
  if (days != null) {
    storeItemRightModelItemsTimePrices.days = days;
  }
  final String? expire = jsonConvert.convert<String>(json['expire']);
  if (expire != null) {
    storeItemRightModelItemsTimePrices.expire = expire;
  }
  final int? price = jsonConvert.convert<int>(json['price']);
  if (price != null) {
    storeItemRightModelItemsTimePrices.price = price;
  }
  final int? originalPrice = jsonConvert.convert<int>(json['originalPrice']);
  if (originalPrice != null) {
    storeItemRightModelItemsTimePrices.originalPrice = originalPrice;
  }
  return storeItemRightModelItemsTimePrices;
}

Map<String, dynamic> $StoreItemRightModelItemsTimePricesToJson(
    StoreItemRightModelItemsTimePrices entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['days'] = entity.days;
  data['expire'] = entity.expire;
  data['price'] = entity.price;
  data['originalPrice'] = entity.originalPrice;
  return data;
}

extension StoreItemRightModelItemsTimePricesExtension
    on StoreItemRightModelItemsTimePrices {
  StoreItemRightModelItemsTimePrices copyWith({
    int? days,
    String? expire,
    int? price,
    int? originalPrice,
  }) {
    return StoreItemRightModelItemsTimePrices()
      ..days = days ?? this.days
      ..expire = expire ?? this.expire
      ..price = price ?? this.price
      ..originalPrice = originalPrice ?? this.originalPrice;
  }
}

StoreItemRightBottomStateEntity $StoreItemRightBottomStateEntityFromJson(
    Map<String, dynamic> json) {
  final StoreItemRightBottomStateEntity storeItemRightBottomStateEntity =
      StoreItemRightBottomStateEntity();
  final String? color = jsonConvert.convert<String>(json['color']);
  if (color != null) {
    storeItemRightBottomStateEntity.color = color;
  }
  final String? message = jsonConvert.convert<String>(json['message']);
  if (message != null) {
    storeItemRightBottomStateEntity.message = message;
  }
  final String? textColor = jsonConvert.convert<String>(json['textColor']);
  if (textColor != null) {
    storeItemRightBottomStateEntity.textColor = textColor;
  }
  return storeItemRightBottomStateEntity;
}

Map<String, dynamic> $StoreItemRightBottomStateEntityToJson(
    StoreItemRightBottomStateEntity entity) {
  final Map<String, dynamic> data = <String, dynamic>{};
  data['color'] = entity.color;
  data['message'] = entity.message;
  data['textColor'] = entity.textColor;
  return data;
}

extension StoreItemRightBottomStateEntityExtension
    on StoreItemRightBottomStateEntity {
  StoreItemRightBottomStateEntity copyWith({
    String? color,
    String? message,
    String? textColor,
  }) {
    return StoreItemRightBottomStateEntity()
      ..color = color ?? this.color
      ..message = message ?? this.message
      ..textColor = textColor ?? this.textColor;
  }
}
