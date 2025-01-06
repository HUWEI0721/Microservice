<template>
  <div class="map-weather-container">
    <!-- 左侧侧边栏 -->
    <div class="sidebar">
      <ul>
        <li @click="selectTab('route')">信息查询</li>
        <li @click="selectTab('holiday')">假日信息</li>
        <li @click="selectTab('brief')">景点简介</li>
        <li @click="selectTab('traffic')">限行查询</li>
        <li @click="selectTab('AI')">AI智能小助手</li>
      </ul>
    </div>

    <!-- 信息展示区域 -->
    <div class="info">
      <div v-if="selectedTab === 'route'" class="guide-container">
        <!-- 路线规划部分 -->
        <div class="weather-header">
          <p style="
              font-size: 20px;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
            ">
            路线规划(步行)🚶‍
          </p>
        </div>
        <div class="route-details">
          <div style="display: inline-block">
            当前选中地址：{{ clickAddress }}
          </div>
          <div>
            <button class="button" @click="setStartPlace">设为起点</button>
            <button class="button" @click="setEndPlace" style="display: inline-block; margin-left: 10px">
              设为终点
            </button>
          </div>
          <div style="margin-top: 8px">
            起点：<span v-if="startPlace">{{ startPlace }}</span>
            <span v-else>未选择起点</span>
          </div>
          <div style="margin-top: 8px">
            终点：<span v-if="endPlace">{{ endPlace }}</span>
            <span v-else>未选择终点</span>
          </div>
          <div style="margin-top: 8px; margin-bottom: 8px">
            <button class="button" @click="PlanRoute">开始规划</button>
            <button class="button" @click="StopPlanRoute" style="margin-left: 10px">
              重新设置
            </button>
          </div>
        </div>

        <p style="
            font-size: 20px;
            text-align: center;
            border-top: 1px solid white;
            border-bottom: 1px solid white;
            padding: 10px 0;
          ">
          出发地与目的地单日天气情况
        </p>
        <!-- 天气情况部分，放在路线规划下方 -->
        <table>
          <thead>
            <tr>
              <th>地点</th>
              <th>温度🌡️</th>
              <th>天气☁️</th>
              <th>湿度💧</th>
              <th>风向🌬</th>
              <th>风力💨</th>
            </tr>
          </thead>
          <tbody>
            <!-- 起点天气信息 -->
            <tr v-if="weatherDataStart">
              <td>{{ sPlace }}</td>
              <td>{{ weatherDataStart.temperature }}°C</td>
              <td>{{ weatherDataStart.weather }}</td>
              <td>{{ weatherDataStart.humidity }}%</td>
              <td>{{ weatherDataStart.winddirection }}风</td>
              <td>{{ weatherDataStart.windpower }}级</td>
            </tr>
            <!-- 终点天气信息 -->
            <tr v-if="weatherDataEnd">
              <td>{{ ePlace }}</td>
              <td>{{ weatherDataEnd.temperature }}°C</td>
              <td>{{ weatherDataEnd.weather }}</td>
              <td>{{ weatherDataEnd.humidity }}%</td>
              <td>{{ weatherDataEnd.winddirection }}风</td>
              <td>{{ weatherDataEnd.windpower }}级</td>
            </tr>
          </tbody>
        </table>

        <p style="
            font-size: 20px;
            text-align: center;
            border-top: 1px solid white;
            border-bottom: 1px solid white;
            padding: 10px 0;
          ">
          一周内上海市天气
        </p>
        <!-- 显示一周的天气走向 -->
        <table>
          <thead>
            <tr>
              <th>星期</th>
              <th>日期</th>
              <th>最低气温</th>
              <th>最高气温</th>
              <th>天气状况</th>
              <th>日出时间</th>
              <th>日落时间</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="day in weatherWeek" :key="day.date">
              <td>{{ day.week }}</td>
              <td>{{ day.date }}</td>
              <td>{{ day.lowest }} °C</td>
              <td>{{ day.highest }} °C</td>
              <td>{{ day.weather }}</td>
              <td>{{ day.sunrise }}</td>
              <td>{{ day.sunset }}</td>
            </tr>
          </tbody>
        </table>
        <!-- 诗句显示（注释说明，根据需求启用） -->
        <div class="poem">
          今日天气诗句：{{ poem.content }} ---{{ poem.author }}《{{
            poem.source
          }}》
        </div>
        <!-- 搜索类别按钮 -->
        <p style="
            font-size: 20px;
            text-align: center;
            border-top: 1px solid white;
            border-bottom: 1px solid white;
            padding: 10px 0;
          ">
          当前选中地址：{{ clickAddress }}附近信息查询
        </p>
        <div style="display: flex; justify-content: center; flex-wrap: wrap">
          <button class="button-choice" @click="searchNearby('美食')">
            美食
          </button>
          <button class="button-choice" @click="searchNearby('酒店')">
            酒店
          </button>
          <button class="button-choice" @click="searchNearby('购物')">
            购物
          </button>
          <button class="button-choice" @click="searchNearby('生活服务')">
            生活服务
          </button>
          <button class="button-choice" @click="searchNearby('旅游景点')">
            旅游景点
          </button>
          <button class="button-choice" @click="searchNearby('休闲娱乐')">
            休闲娱乐
          </button>
          <button class="button-choice" @click="searchNearby('医疗')">
            医疗
          </button>
        </div>

        <div v-if="nearbyPlaces.length > 0" style="margin-left: 10px">
          <ul>
            <li v-for="place in nearbyPlaces" :key="place.uid">
              <p><strong>名称：</strong>{{ place.name }}</p>
              <p><strong>地址：</strong>{{ place.address }}</p>
              <p><strong>城市：</strong>{{ place.city }}</p>
              <p><strong>区域：</strong>{{ place.area }}</p>
              <p v-if="place.telephone">
                <strong>电话：</strong>{{ place.telephone }}
              </p>
            </li>
          </ul>
        </div>
        <div class="weather-header">
          <p style="
              font-size: 20px;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
            ">
            地点“{{ searchPlace }}”wiki百科🔍
          </p>
        </div>
        <div class="searchresult">
          <div v-for="page in WikiPage" :key="page.title">
            <div class="page-item">
              <div class="page-content">
                <a :href="page.url" target="_blank" style="color: rgb(221, 110, 132); font-weight: bold">
                  {{ page.title }}
                </a>
                <p style="font-size: 20px">{{ page.extract }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="selectedTab === 'holiday'" class="brief-introduction">
        <!-- 假日信息部分 -->
        <div class="weather-header">
          <p style="
              font-size: 20px;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
            ">
            节假日查询📅
          </p>
        </div>
        <div>
          <select v-model="selectedMonth" style="
              margin-left: 10px;
              height: 41px;
              border-radius: 20px;
              font-size: 30px;
            ">
            <option value="1">一月</option>
            <option value="2">二月</option>
            <option value="3">三月</option>
            <option value="4">四月</option>
            <option value="5">五月</option>
            <option value="6">六月</option>
            <option value="7">七月</option>
            <option value="8">八月</option>
            <option value="9">九月</option>
            <option value="10">十月</option>
            <option value="11">十一月</option>
            <option value="12">十二月</option>
          </select>
          <button class="button" @click="getHoliday(selectedMonth)">
            查询节假日
          </button>
        </div>
        <div class="briefintro-searchresult">
          <ul style="padding-left: 0px">
            <li v-for="item in holiday" :key="item">
              <p style="color: gold; text-align: center; position: relative">
                {{ item.date }} {{ item.cnweekday }} {{ item.name }}
                <span style="
                    display: block;
                    position: absolute;
                    top: 0;
                    left: 50%;
                    width: 50%;
                    height: 1px;
                    background-color: white;
                    transform: translateX(-50%);
                  "></span>
                <span style="
                    display: block;
                    position: absolute;
                    bottom: 0;
                    left: 50%;
                    width: 50%;
                    height: 1px;
                    background-color: white;
                    transform: translateX(-50%);
                  "></span>
              </p>

              <p style="color: #ccc; font-size: 15px; text-align: center">
                {{ item.lunaryear }} {{ item.lunarmonth }} {{ item.lunarday }}
              </p>
              <p style="color: #ccc; font-size: 16px; text-align: center">
                {{ item.tip }}
              </p>
            </li>
          </ul>
        </div>
      </div>

      <div v-if="selectedTab === 'brief'" class="brief-introduction">
        <!-- 景点简介部分 -->
        <div class="weather-header">
          <p style="
              font-size: 20px;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
            ">
            景点简介📖
          </p>
          <span style="
              color: #aaa;
              font-size: 20px;
              font-weight: normal;
              text-align: center;
            ">
            下面是对"{{ searchPlace }}"的简介
          </span>
        </div>
        <div class="briefintro-searchresult">
          <div class="briefintro-content" v-html="briefIntro"></div>
        </div>

        <div class="weather-header">
          <p style="
              font-size: 20px;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
            ">
            景点预约🗓️
          </p>
        </div>
        <div class="reservation-list">
          <ul style="padding-left: 20px">
            <li v-for="site in reservationSites" :key="site.name" style="padding-bottom: 10px">
              <a :href="site.url" target="_blank" style="color: white; font-weight: bold">
                {{ site.name }}
              </a>
            </li>
          </ul>
        </div>
        <div style="text-align: center">
          <a href="https://service.shmetro.com/yxxlt/index.htm" target="_blank" style="
              color: white;
              text-decoration: none;
              display: inline-block;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
              width: 100%;
            ">
            上海地铁官网🚇
          </a>
        </div>
      </div>

      <div v-if="selectedTab === 'traffic'" class="traffic-container">
        <div class="weather-header">
          <p style="
              font-size: 20px;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
            ">
            限行查询🚧
          </p>
        </div>
        <div class="traffic-details">
          <p>城市：上海</p>
          <div v-for="limit in this.trafficData.limitinfo" :key="limit.type">
            <h3>{{ limit.type }}</h3>
            <div v-for="remark in limit.remark" :key="remark.rule">
              <p><strong>限行区域：</strong>{{ remark.zone }}</p>
              <p><strong>规则：</strong>{{ remark.rule }}</p>
              <p><strong>时间：</strong>{{ remark.time }}</p>
            </div>
          </div>
        </div>
      </div>
      <div v-if="selectedTab === 'AI'" class="AI-container">
        <!-- AI智能小助手部分 -->
        <div class="weather-header">
          <p style="
              font-size: 20px;
              text-align: center;
              border-top: 1px solid white;
              border-bottom: 1px solid white;
              padding: 10px 0;
            ">
            AI智能小助手🤖<br />
          </p>
          <p>
            您可以直接点击穿衣建议、出行建议、旅游建议获取对应信息<br />
            也可以输入你想查找的信息点击通用建议以获取信息
          </p>
          <input class="input-box" type="text" v-model="userInput" placeholder="请输入你想查询的内容" />
          <div>
            <div class="weather-dialog-box">
              <div class="dialog-content">{{ dialogBox }}</div>
            </div>
            <div>
              <button class="button" id="dress-advice-btn" @click="getDressAdvice">
                穿衣建议
              </button>
              <button class="button" id="travel-advice-btn" @click="getTravelAdvice">
                出行建议
              </button>
              <button class="button" id="weather-summary-btn" @click="getTripAdvice">
                旅行建议
              </button>
              <button class="button" @click="getAdvice">通用建议</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 地图显示区域 -->
    <div class="map">
      <!-- 百度地图组件，设置地图中心、缩放级别及滚轮缩放 -->
      <baidu-map class="map" :center="center" :zoom="zoom" :scroll-wheel-zoom="true" @click="handleMapClick">
        <!-- 步行路线规划，依据 startPosition 和 endPosition -->
        <bm-walking v-if="planRoute" :start="startPosition" :end="endPosition" location="上海" :auto-viewport="true"
          :selectFirstResult="true" :panel="true">
        </bm-walking>
      </baidu-map>
    </div>
  </div>
</template>

<script defer="true">
import {
  BaiduMap,
  BmWalking,
  BmInfoWindow,
  BmBoundary,
  BmScale,
  BmMarker,
  BmLabel,
  BmLocalSearch,
  BmCircle,
  BmBus,
} from "vue-baidu-map-3x";
import { ref, onMounted } from "vue";
import axios from "axios";
import OpenAI from "openai";
import Qs from "qs";

export default {
  components: {
    BaiduMap,
    BmWalking,
  },

  data() {
    return {
      reservationSites: [
        { name: "上海迪士尼", url: "https://www.shanghaidisneyresort.com/" },
        { name: "东方明珠电视塔", url: "http://www.orientalpearltower.com/" },
        { name: "上海野生动物园", url: "http://www.shwoo.com/" },
        { name: "上海科技馆", url: "http://www.sstm.org.cn/" },
        { name: "上海博物馆", url: "http://www.shanghaimuseum.net/" },
        { name: "上海天文馆", url: "https://www.sstm-sam.org.cn/" },
        { name: "豫园", url: "http://www.yugarden.com.cn" },
        { name: "上海海昌海洋公园", url: "https://www.haichangoceanpark.com/shanghai/", },
        { name: "静安寺", url: "https://www.shjas.com/" },
        { name: "上海之巅观光厅", url: "https://www.shanghaitower.com/Home1.html", },
        { name: "上海自然博物馆", url: "http://www.snhm.org.cn/" },
        { name: "上海世博会博物馆", url: "http://www.expo-museum.org/" },
        { name: "上海杜莎夫人蜡像馆", url: "https://www1.madametussauds.com/shanghai/plan-your-visit/", },
      ],

      weatherDataStart: null,
      weatherDataEnd: null,
      userInput: "", // 用户输入的内容
      mysubway: null,
      selectedTab: "route",
      center: { lat: 31.23, lng: 121.474 },
      zoom: 15,
      weatherData: null, //存储天气数据
      city: "310000", //上海
      selectedMarkerIndex: -1, // 记录选中的标记索引
      dialogBox: "点击下面的按钮以获得智能小助手建议～",
      WikiPage: [], // 新添加的空数组
      searchPlace: "上海",
      briefIntro: "~请在右侧地图上点击你想要查询的景区~",
      poem: "",

      nearbyPlaces: [],
      //点击获取点击地点的经度和纬度
      clickLat: null,
      clickLng: null,
      clickAddress: "当前未选择地点",

      addressComponent: null,
      sPlace: null,
      ePlace: null,

      //设置搜索范围
      nearby: {
        center: {
          lng: 121.474,
          lat: 31.23,
        },
        radius: 10000,
      },
      placeKeyword: "",
      startPlace: "",
      startPosition: null,

      endPlace: "",
      endPosition: null,

      planRoute: false,
      weatherWeek: null,
      dressAdvice: "",
      holiday: "",
      selectedMonth: new Date().getMonth() + 1, // 用户选择的月份，默认为当前月份
      trafficData: null,
    };
  },

  created() {
    // if(this.weatherData!=null)
    this.checkTrafficRestrictions(); // 自动查询限行信息
    this.getWeatherPoem();
    this.getWeekWeather();
    this.getHoliday(10);
  },

  mounted() {
    this.mapInstance = this.$refs.map ? this.$refs.map.getMap() : null;
    this.searchWiki("上海");
  },

  methods: {
    selectTab(tab) {
      this.selectedTab = tab; // 设置选中的功能
    },

    handleMapClick(event) {
      const point = new BMap.Point(event.point.lng, event.point.lat);
      const geoc = new BMap.Geocoder();

      // 调用百度地图 API 的逆地理编码方法获取地名
      geoc.getLocation(point, (result) => {
        if (result && result.addressComponents) {
          const lng = result.point.lng; // 经度
          const lat = result.point.lat; // 纬度
          console.log(lng, lat);

          // 配置API请求的参数
          const params = {
            ak: "BWEI4re5yktUgvQSlxTZAp0VpSfnO8oD", // 替换为你自己的API Key
            location: `${lat},${lng}`, // 经纬度
            extensions_poi: 1, // 请求POI数据
            output: "json", // 输出格式
          };

          console.log(params);

          // 使用axios发起请求并附加参数
          axios
            .get("http://121.40.116.78:3000/api/reverse_geocoding", { params })
            .then((response) => {
              console.log(response.data);
              const data = response.data;
              if (data.status === 0) {
                //console.log(data.result.pois);
                this.addressComponent = data.result.addressComponent;
                const address = data.result.formatted_address; // 结构化地址
                console.log(address);
                const pois = data.result.pois || []; // 周边地标信息
                const placeName = pois.length > 0 ? pois[0].name : "无附近地标"; // 获取最可能的地标名称
                //console.log(placeName);

                this.clickLat = lat;
                this.clickLng = lng;
                this.clickAddress = address;
                console.log(this.clickAddress);
                this.searchPlace = placeName; // 保存地点名称到searchPlace字段
                console.log(this.searchPlace);
                this.getBriefIntro(placeName); // 调用景点介绍API
                console.log("调用简介api");
                this.searchWiki(placeName); // 调用Wiki百科API

                // 输出信息
                console.log("地点名称:", placeName);
                console.log("经度:", lng);
                console.log("纬度:", lat);
                console.log("地址:", address);
              } else {
                console.log("逆地理编码查询失败", data.message);
              }
            })
            .catch((error) => {
              console.error("请求出错", error);
            });
        } else {
          console.log("未找到相关地点信息");
        }
      });
    },

    searchNearby(type) {
      if (
        !this.clickLat ||
        !this.clickLng ||
        this.clickAddress == "当前未选择地点"
      ) {
        alert("请先点击地图选择一个位置！");
        return;
      }

      // 调用代理服务器的 API 进行圆形区域搜索
      const apiURL = `http://121.40.116.78:3000/api/circle_search`;

      axios
        .get(apiURL, {
          params: {
            query: type,
            location: `${this.clickLat},${this.clickLng}`,
            radius: 2000,
            output: "json",
            ak: "BWEI4re5yktUgvQSlxTZAp0VpSfnO8oD",
          },
        })
        .then((response) => {
          if (response.data.status === 0) {
            this.nearbyPlaces = response.data.results; // 保存返回的地点信息
            console.log(response.data.results);
          } else {
            console.error("搜索失败:", response.data.message);
          }
        })
        .catch((error) => {
          console.error("API请求出错:", error);
        });
    },

    // 设置为起点
    async setStartPlace() {
      if (
        !this.clickLat ||
        !this.clickLng ||
        this.clickAddress === "当前未选择地点"
      ) {
        alert("请先点击地图选择一个位置！");
        return;
      }
      this.sPlace = this.addressComponent.district;
      // 使用 await 等待天气数据获取
      this.weatherDataStart = await this.getWeatherData(this.sPlace);
      console.log(this.weatherDataStart);
      this.startPlace = this.clickAddress;
      this.startPosition = { lat: this.clickLat, lng: this.clickLng };
    },

    // 设置为终点
    async setEndPlace() {
      if (
        !this.clickLat ||
        !this.clickLng ||
        this.clickAddress === "当前未选择地点"
      ) {
        alert("请先点击地图选择一个位置！");
        return;
      }
      this.ePlace = this.addressComponent.district;
      // 使用 await 等待天气数据获取
      this.weatherDataEnd = await this.getWeatherData(this.ePlace);
      console.log(this.weatherDataEnd);
      this.endPlace = this.clickAddress;
      this.endPosition = { lat: this.clickLat, lng: this.clickLng };
    },

    PlanRoute() {
      if (this.startPlace != "" && this.endPlace != "") {
        this.planRoute = true;
      } else {
        // 弹窗提醒用户设置起点和终点
        alert("请设置起点和终点");
      }
    },

    StopPlanRoute() {
      this.planRoute = false;
      this.startPlace = "";
      this.endPlace = "";
    },

    //调用天气api，已经可以调取成功
    async getWeatherData(place) {
      console.log(place);
      const key = "3df1b4f2095a903b7822ab11f34f5a11"; // 高德开发者key
      const url = `https://restapi.amap.com/v3/weather/weatherInfo?key=${key}&city=${place}`;

      console.log("获取天气");

      try {
        const response = await axios.get(url); // 使用 await 等待请求结果

        console.log(response.status);

        if (
          response.status === 200 &&
          response.data &&
          response.data.lives &&
          response.data.lives.length > 0
        ) {
          console.log(response.data.lives[0]);
          return response.data.lives[0]; // 当 response.data.lives 不为空时返回数据
        } else {
          console.log("没有获取到有效的天气信息");
          return null; // 当 lives 为空时返回 null
        }
      } catch (error) {
        console.error("请求发生错误:", error);
        return null; // 捕获异常时返回 null
      }
    },

    //调用天气诗句api
    getWeatherPoem() {
      axios({
        method: "post",
        url: "https://apis.tianapi.com/tianqishiju/index",
        data: Qs.stringify({
          key: "a86dc8f29b4bd236a56243267e9d2ad3",
          tqtype: 2,
        }),
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      }).then((res) => {
        this.poem = res.data.result;
      });
    },

    //得到一周的天气
    getWeekWeather() {
      axios({
        method: "post",
        url: "https://apis.tianapi.com/tianqi/index",
        data: Qs.stringify({
          key: "a86dc8f29b4bd236a56243267e9d2ad3",
          city: "101020100",
          type: "7",
        }),
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      }).then((res) => {
        console.log(res.data.result);
        this.dressAdvice = res.data.result.list[0].tips;
        this.weatherWeek = res.data.result.list.map((item) => ({
          week: item.week,
          date: item.date,
          lowest: parseFloat(item.lowest),
          highest: parseFloat(item.highest),
          weather: item.weather,
          sunrise: item.sunrise,
          sunset: item.sunset,
        }));
        console.log(this.weatherWeek);
      });
    },

    //调用Wiki百科API
    async searchWiki(keyword) {
      try {
        const response = await axios.get("https://zh.wikipedia.org/w/api.php", {
          params: {
            format: "json",
            action: "query",
            generator: "search",
            gsrnamespace: 0,
            gsrlimit: 5,
            prop: "pageimages|extracts",
            pilimit: "max",
            exintro: true,
            explaintext: true,
            exsentences: 1,
            exlimit: "max",
            origin: "*",
            gsrsearch: keyword,
          },
        });
        const pages = response.data.query.pages;
        const wikiPages = [];
        for (let pageId in pages) {
          const page = pages[pageId];
          const wikiPage = {
            title: page.title,
            thumbnail: page.thumbnail,
            extract: page.extract,
            url: "",
          };
          // 将title拼接成访问中文维基百科的链接
          wikiPage.url =
            "http://zh.wikipedia.org/wiki/" + encodeURIComponent(page.title);
          wikiPages.push(wikiPage);
        }
        this.WikiPage = wikiPages;
        console.log(this.WikiPage);
      } catch (error) {
        console.error("Error:", error);
      }
    },

    //得到景点介绍
    getBriefIntro(place) {
      const details =
        "请你介绍一下有关上海市" +
        place +
        "的相关信息，并给出一些合适的游玩此地的建议";
      this.briefIntro = "正在搜索中...请稍后~";
      console.log(details);
      this.getReplyGPT(details, 0);
    },

    //调用查询节假日的接口，得到这个月的节假日
    getHoliday(month) {
      // 判断月份是否为个位数，如果是则在前面添加一个零
      var formattedMonth = month < 10 ? "0" + month : month;
      var Date = "2024-" + formattedMonth;
      console.log(Date);
      axios({
        method: "post",
        url: "https://apis.tianapi.com/jiejiari/index",
        data: Qs.stringify({
          key: "a86dc8f29b4bd236a56243267e9d2ad3",
          date: Date,
          type: 2,
        }),
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      }).then((res) => {
        this.holiday = res.data.result.list.filter(
          (item) => item.isnotwork === 1
        );
        console.log(this.holiday);
      });
    },

    //点击穿衣按钮之后获取穿衣建议
    getDressAdvice() {
      const details =
        "已知今天上海的天气数据如下所示：" +
        "湿度：" +
        this.weatherData.humidity +
        "，温度：" +
        this.weatherData.temperature +
        "，天气：" +
        this.weatherData.weather +
        "，风力：" +
        this.weatherData.windpower +
        "，请问可以给出穿衣建议吗？可以把下面这段话扩写一下变得更加活泼俏皮：" +
        this.dressAdvice +
        ",直接生成一段话，50到80字左右，不要列出一点一点的，也不要太长）";
      console.log(details);
      this.getReplyGPT(details, 1);
      this.showResponse();
    },

    //点击出行按钮之后获取出行建议
    getTravelAdvice() {
      const details =
        "已知今天上海的天气数据如下所示：" +
        "湿度：" +
        this.weatherData.humidity +
        "，温度：" +
        this.weatherData.temperature +
        "，天气：" +
        this.weatherData.weather +
        "，风力：" +
        this.weatherData.windpower +
        "，请问可以给出出行建议吗？就根据我给你的上海目前的天气信息给出，比如说如果今天下雨就建议打车或者地铁，如果今天天晴就建议骑自行车或者走路，如果风大就尽量不要骑车等等。（直接生成一段话，50到80字左右，不要列出一点一点的，也不要太长）";
      console.log(details);
      this.getReplyGPT(details, 1);
      this.showResponse();
    },

    //点击天气概述按钮之后获取天气概述
    getTripAdvice() {
      const details =
        "已知今天上海的天气数据如下所示：" +
        "湿度：" +
        this.weatherData.humidity +
        "，温度：" +
        this.weatherData.temperature +
        "，天气：" +
        this.weatherData.weather +
        "，风力：" +
        this.weatherData.windpower +
        "，请你结合当日的天气给出当日的天气概述，并给出人们合适的旅行建议，一段话，60~90字左右";
      console.log(details);
      this.getReplyGPT(details, 1);
      this.showResponse();
    },

    getAdvice() {
      const details = this.userInput;
      console.log(details);
      this.getReplyGPT(details, 1);
      this.showResponse();
    },

    showResponse() {
      // 将回复显示在对话框中
      this.dialogBox = "正在生成中......";
    },

    async getReplyGPT(details, choice) {
      const API_KEY = "";
      const openai = new OpenAI({
        apiKey: API_KEY,
        dangerouslyAllowBrowser: true,
      });
      console.log("正在生成介绍" + details + "的信息");
      const chatCompletion = await openai.chat.completions.create({
        messages: [
          {
            role: "user",
            content: `你好，${details}`,
          },
        ],
        model: "gpt-3.5-turbo",
      });
      console.log(chatCompletion);
      console.log(chatCompletion.choices[0].message.content);
      // 说明此时是在生成智能出行小助手
      if (choice == 1) {
        //显示在消息框内
        this.dialogBox = chatCompletion.choices[0].message.content;
      } else {
        this.briefIntro = chatCompletion.choices[0].message.content;
      }
    },

    async checkTrafficRestrictions() {
      this.weatherData = await this.getWeatherData("上海");
      console.log(this.weatherData);
      axios({
        method: "post",
        url: "https://apis.tianapi.com/vehiclelimit/index",
        data: Qs.stringify({
          key: "a86dc8f29b4bd236a56243267e9d2ad3",
          city: "上海",
        }),
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      }).then((res) => {
        this.trafficData = res.data.result;
        console.log(res.data.result);
      });
    },
  },
};
</script>

<style scoped>
.reservation-list {
  margin-left: 28px;
  color: white;
}

.button {
  background-color: white;
  color: rgb(255, 0, 119);
  border: none;
  border-radius: 20px;
  display: inline-block;
  margin-left: 20px;
  margin-top: 10px;
  margin-right: 10px;
  padding: 10px;
  font-size: 16px;
  cursor: pointer;
}

.button-choice {
  background-color: white;
  color: rgb(255, 0, 119);
  border: none;
  border-radius: 20px;
  display: inline-block;
  margin-left: 5px;
  margin-top: 5px;
  margin-right: 5px;
  padding: 5px;
  font-size: 16px;
  cursor: pointer;
}

table {
  margin: 0 auto;
  /* 水平居中 */
  font-size: 15px;
  border-collapse: separate;
  /* 使表格边框分开 */
  border-spacing: 0 10px;
  /* 设置行间距为10px */
}

th,
td {
  padding: 10px;
  text-align: center;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.traffic-details p {
  line-height: 1.5;
  /* 设置行距为1.5倍 */
}

.map-weather-container {
  display: grid;
  grid-template-columns: 200px 1fr 1fr;
  /* 将容器分为左右三列，第一列为侧边栏，第二列为信息展示区，第三列为地图 */
  width: 100%;
  height: 100vh;
  overflow-y: hidden;
}

.sidebar {
  background-color: #1bffe8;
  padding: 10px;
  color: white;
}

.sidebar ul {
  list-style: none;
  padding: 0;
}

.sidebar ul li {
  height: 40px;
  cursor: pointer;
  padding: 10px;
  background-color: #0c4bfb;
  color: white;
  margin-bottom: 5px;
  text-align: center;
  /* 可选，Flexbox 已经处理居中 */
  font-weight: bold;
  border-radius: 5px;
  display: flex;
  /* 使用 Flexbox */
  align-items: center;
  /* 垂直居中 */
  justify-content: center;
  /* 水平居中 */
}

.sidebar ul li:hover {
  background-color: #1b365d;
  color: #fff;
}

.info {
  grid-column: 2 / 3;
  /* 占据左半部分 */
  overflow-y: scroll;
  max-height: 100vh;
  width: 100%;
  background-color: transparent;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.233);
  color: #fff;
  /* 给 info 添加一些内边距 */
}

.map {
  grid-column: 3 / 4;
  /* 占据右半部分 */
  width: 100%;
  height: 100vh;
}

.default-message {
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  height: 100%;
  font-size: 18px;
  line-height: 1.5;
  color: black;
}


.AI-container,
.traffic-container,
.guide-container,
.brief-introduction {
  width: 100%;
  height: fit-content;
  background-color: #077dddfc;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.233);
  color: #fff;
}

.route-details {
  margin-left: 28px;
}

.weather-header {
  padding-top: 20px;
  padding-bottom: 10px;
}

.weather-details {
  margin-left: 28px;
}

.input-box {
  width: 80%;
  height: 40px;
  margin-top: 10px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 10px;
  border-radius: 10px;
  border: none;
  background-color: whitesmoke;
  font-size: 20px;
}

.weather-dialog-box {
  width: 80%;
  height: 110px;
  margin-top: 10px;
  margin-left: 20px;
  background-color: whitesmoke;
  border-radius: 6px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 10px;
  overflow: auto;
  max-height: 100px;
}

.dialog-content {
  white-space: pre-wrap;
  color: rgb(34, 33, 33);
}

.briefintro-content {
  margin-left: 28px;
  margin-right: 20px;
  margin-top: 10px;
  margin-bottom: 20px;
  font-size: 18px;
  line-height: 1.5;
}

.briefintro-searchresult {
  max-height: fit-content;
}

.searchresult {
  max-height: fit-content;
  overflow: auto;
  margin: 0px 5px 0px 20px;
  font-size: 18px;
}

.page-item {
  display: flex;
  margin-top: 10px;
}

.page-item img {
  width: 100px;
  height: 100px;
  margin-right: 10px;
}

.page-content {
  flex: 1;
}

.traffic-details {
  margin-left: 20px;
  margin-right: 10px;
  margin-top: 10px;
}

.poem {
  margin-top: 10px;
  padding-bottom: 20px;
  margin-left: 20px;
  font-size: 16px;
  color: red;
}
</style>