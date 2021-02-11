module.exports = {
    title: '开源社区系统 — Echo',  // 设置网站标题
    description : '一款基于 SpringBoot + MyBatis + MySQL + Redis + Kafka + Elasticsearch + ... 实现的开源社区系统，并提供详细的开发文档和配套教程',
    base : '/Echo/',
    themeConfig : {
      nav : [
        {
            text: '仓库地址',
            items: [
              { text: 'Github', link: 'https://github.com/Veal98/Echo' },
              { text: 'Gitee', link: 'https://gitee.com/veal98/echo' }
            ]
        },
        { text: '体验项目', link: 'http://1.15.127.74:8080/' },
        { text: '配套教程', link: '/error' }
      ],
      sidebar: 'auto', // 侧边栏配置
      sidebarDepth : 2,
      lastUpdated: 'Last Updated', // string | boolean
      
    }
  }