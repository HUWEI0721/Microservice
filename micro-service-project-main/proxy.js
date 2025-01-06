const express = require('express');
const axios = require('axios');
const cors = require('cors'); // 引入 cors
const app = express();
const port = 3000;

app.use(cors()); // 使用 cors 中间件
app.use(express.json());

// 逆地理编码接口
app.get('/api/reverse_geocoding', async (req, res) => {
    const { location, ak, extensions_poi, output } = req.query;
    try {
        const response = await axios.get('https://api.map.baidu.com/reverse_geocoding/v3/', {
            params: { location, ak, extensions_poi, output },
        });
        res.json(response.data);
    } catch (error) {
        res.status(500).send('请求失败');
    }
});

// 新增圆形区域搜索接口
app.get('/api/circle_search', async (req, res) => {
    const { query, location, radius, ak, output } = req.query;
    try {
        const response = await axios.get('https://api.map.baidu.com/place/v2/search', {
            params: { query, location, radius, ak, output },
        });
        res.json(response.data);
    } catch (error) {
        res.status(500).send('请求失败');
    }
});

// 启动服务器
app.listen(port, () => {
    console.log(`Proxy server running at http://localhost:${port}`);
});
