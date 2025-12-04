import 'dart:convert';

import 'package:flutter_creat_module/generated/json/base/json_field.dart';
import 'package:flutter_creat_module/generated/json/store_item_right_model_entity.g.dart';
import 'package:flutter_creat_module/pages/me/model/me_friend_items_entity.dart';
import 'package:flutter_creat_module/pages/me/model/me_tab_properties_entity.dart';

@JsonSerializable()
class StoreItemRightModelEntity {
  List<StoreItemRightModelItems>? items = [];
  int? type = 0;
  bool? isLoading = false;

  StoreItemRightModelEntity({this.isLoading = false});

  factory StoreItemRightModelEntity.fromJson(Map<String, dynamic> json) =>
      $StoreItemRightModelEntityFromJson(json);

  Map<String, dynamic> toJson() => $StoreItemRightModelEntityToJson(this);

  @override
  String toString() {
    return jsonEncode(this);
  }
}

@JsonSerializable()
class StoreItemRightModelItems {
  String? id = '';
  int? type = 0;
  int? needAsset = 0;
  int? price = 0;
  String? name = '';
  String? icon = '';
  String? resUrl = '';
  String? expire = '';
  int? expireDays = 0;
  List<StoreItemRightModelItemsTimePrices>? timePrices = [];
  String? tips = '';
  String? desc = '';
  String? title = '';
  int? update = 0;
  int? rarity = 0;
  int? privilege = 0;
  int? reviewStatus = 0;
  int? state = 0;
  String? privilegeName = '';
  String? privilegeNameColor = '';
  int? customIdLen = 0;
  String? friendUid = '';
  List<MeTabPropertiesProperties>? properties = [];
  StoreItemRightBottomStateEntity bottomState =
      StoreItemRightBottomStateEntity();
  List<MeFriendItemsFriendItems>? friendItems = [];

  StoreItemRightModelItems();

  factory StoreItemRightModelItems.fromJson(Map<String, dynamic> json) =>
      $StoreItemRightModelItemsFromJson(json);

  Map<String, dynamic> toJson() => $StoreItemRightModelItemsToJson(this);

  @override
  String toString() {
    return jsonEncode(this);
  }
}

@JsonSerializable()
class StoreItemRightModelItemsTimePrices {
  int? days = 0;
  String? expire = '';
  int? price = 0;
  int? originalPrice = 0;

  StoreItemRightModelItemsTimePrices();

  factory StoreItemRightModelItemsTimePrices.fromJson(
          Map<String, dynamic> json) =>
      $StoreItemRightModelItemsTimePricesFromJson(json);

  Map<String, dynamic> toJson() =>
      $StoreItemRightModelItemsTimePricesToJson(this);

  @override
  String toString() {
    return jsonEncode(this);
  }
}

@JsonSerializable()
class StoreItemRightBottomStateEntity {
  String? color = '';
  String? message = '';
  String? textColor = '';

  StoreItemRightBottomStateEntity();

  factory StoreItemRightBottomStateEntity.fromJson(Map<String, dynamic> json) =>
      $StoreItemRightBottomStateEntityFromJson(json);

  Map<String, dynamic> toJson() => $StoreItemRightBottomStateEntityToJson(this);

  @override
  String toString() {
    return jsonEncode(this);
  }
}
