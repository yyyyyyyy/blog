import Vue from 'vue'
import Router from 'vue-router'
import Blog from '@/components/Blog'
import Write from '@/components/Write'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Blog',
      component: Blog
    }, {
      path: '/write',
      name: 'Write',
      component: Write
    }
  ]
})
