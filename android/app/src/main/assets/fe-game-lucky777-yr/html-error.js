function GetUserInfo() {
  let userBase64 = null;
  if (/(Android)/i.test(navigator.userAgent))
  {
    if (window.fun)
    {
      try
      {
        userBase64 = window.fun.getUserInfo("");
      } catch (error)
      {
        userBase64 = window.fun.getUserInfoNew("");
      }
    }
  } else
  {
    try {
      userBase64 = window.prompt('getUserInfo');
    } catch (error) {
      userBase64 = window.prompt('getUserInfoNew');
    }
  }
  if (userBase64)
  {
    try {
      const userStr = window.atob(userBase64);
      const newUser = JSON.parse(userStr)
      if (newUser && newUser.params)
      {
        return JSON.parse(newUser.params);
      }
    } catch (error) {
      return JSON.parse(userBase64);
    }
  }
}

var sendTimes = 0;
var userObj = null;
function ReportEvent(
  event,
  data,
  reportVn = true
)
{
  if (!userObj) {
    userObj = GetUserInfo();
  }
  if (!userObj || !userObj.token)
    {
      return;
    }
    if (!userObj.deviceId)
      {
        return;
      }
      
  const formattedData = {
    params: {
      ...data,
      package: userObj.packageName,
    },
    event,
    reportVn,
  };
  const headData = {
    appVersion: userObj.appVersion,
    deviceId: userObj.deviceId,
    token: userObj.token,
    package: userObj.packageName,
    df: "fe-common-event",
  };
  const reportUrl = 'https://api.yriry.com/v2/client-event/report';
  sendTimes++;
  setTimeout(() => {
    sendHttpData(
      reportUrl,
      formattedData,
      null,
      null,
      "POST",
      5000,
      headData
    );
  }, 1000 * sendTimes);
};

function sendHttpData(
  url,
  data,
  completeCallback,
  errorCallback,
  method = "GET",
  timeout = 5000,
  requestHeaderDic
) {
  const xhr = new XMLHttpRequest();
  xhr.responseType = 'json';
  xhr.timeout = timeout;
  if (method === "GET" && data != null)
  {
    const paramUrl = this.serializeGetParams(data);
    if (paramUrl)
    {
      url = url + "?" + paramUrl;
    }
  }
  xhr.onload = () => {
    const status = xhr.status;
    // 响应内容解析完成
    if (status === 200)
    {
      // 执行成功后的回调
      if (completeCallback != null)
      {
        completeCallback(xhr.response);
      }
    }
    // 重定向
    else if (status === 301)
    {
      this.sendHttpData(
        xhr.getResponseHeader("Location") || "",
        data,
        completeCallback,
        errorCallback,
        method,
        timeout,
        requestHeaderDic
      );
    } else if (status === 500)
    {
      if (errorCallback)
      {
        errorCallback(xhr.response);
      }
    }
  };
  xhr.open(method, url, true);
  xhr.setRequestHeader("Content-type", "application/json;charset=utf-8");
  // 设置请求头部数据
  for (const key in requestHeaderDic)
  {
    if (Object.prototype.hasOwnProperty.call(requestHeaderDic, key))
    {
      xhr.setRequestHeader(key, encodeURI(requestHeaderDic[key]));
    }
  }
  if (method === "GET")
  {
    xhr.send();
  } else if (method === "POST")
  {
    if (data == null)
    {
      data = {};
    }
    xhr.send(JSON.stringify(data));
  }
};

// 重写错误日志，添加错误上报
const originalConsoleError = console.error;
console.error =  (...args)=> {
    // 构造可序列化的错误对象
    const errorData = {
        message: args.map(arg => {
            try {
                return typeof arg === 'object' ? JSON.stringify(arg) : String(arg);
            } catch {
                return '[Unserializable Object]';
            }
        }).join(' '),
        stack: new Error().stack
    };
    // 上报错误事件
    ReportEvent('fe-game-console-error', {
        error: errorData,
        duration: Math.floor(window.performance.now()),
        webview: window.navigator.userAgent,
        url: window.location.href,
        game_version: window['Version'] || 'unknown',
        cocos_version: window['cocos_version'] || 'unknown'
    });

    // 调用原始方法
    try {
        originalConsoleError.apply(console, args);
    } catch (e) {
        // 备用调用方式
        Function.prototype.apply.call(originalConsoleError, console, args);
    }
};

window.onerror =  (...args) => {
  let str1 = '';
  args.forEach(function (element) {
    str1 += element;
  });
  if (str1.includes('ReferenceError')
    || str1.includes('error.00null')
    || str1.includes('window.game.netEventManger')
    || str1.includes('FunAsyncJSBridgeCallback')
    || str1.includes('__fun_visibilitychange')
  ) {
    return;
  }
  ReportEvent('fe-game-html-error', { error: str1, duration: Math.floor(window.performance.now()), webview: window.navigator.userAgent,url: window.location.href ,game_version: window['Version'],cocos_version: window['cocos_version']});
};
window['cocos_version'] = 'v0.0.11';
var host = window.location.host;
ReportEvent('fe-game-html-init', {  duration: Math.floor(window.performance.now()), webview: window.navigator.userAgent,url: window.location.href ,game_version: window['Version'],cocos_version: window['cocos_version']});
var url = `//${host}/game/fe-game-cocos-js-yr/cc-${window['cocos_version']}.js`;
var script = document.createElement('script');
script.type = 'systemjs-importmap';
script.charset = 'utf-8';
script.innerHTML = JSON.stringify({ imports: { cc: url } });
document.head.appendChild(script);