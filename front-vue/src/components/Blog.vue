<template>
  <div class="log">
    <div class="content-list">
      <ol>
        <li class="li-title">
          Note List
        </li>
        <li class="li-note" @click="get_blog_by_id(blog.id)" v-for="blog in blogs" :key="blog.id">
          {{blog.name}}
        </li>
      </ol>
    </div>
    <div class="content-content" v-if="on_flag">
      <mavon-editor :editable="false" :subfield="false" :defaultOpen="preview" :toolbars="toolbars"
                    v-model="current_blog.content"/>
    </div>
  </div>
</template>
<script>
  import axios from 'axios'

  export default {
    name: 'Blog',
    data() {
      return {
        blogs: [],
        current_blog: {},
        on_flag: false,
        preview: 'preview',
        toolbars: {
          readmodel: true
        }
      }
    },
    methods: {
      list_blog() {
        axios.get(this.global.serverPath + '/blog/listBlogs', {
          headers: {
            'Content-Type': 'application/json',
            withCredentials: true
          }
        }).then((response) => {
          if (response.data.success) {
            this.blogs = response.data.result
          }
        }).catch((response) => {
          console.info(response)
        })
      },
      get_blog_by_id(id) {
        axios.get(this.global.serverPath + '/blog/getBlogById/' + id, {
          headers: {
            'Content-Type': 'application/json',
            withCredentials: true
          }
        }).then((response) => {
          if (response.data.success) {
            if (response.data.result) {
              this.current_blog = response.data.result
              this.on_flag = true
            }
          }
        }).catch((response) => {
          console.info(response)
          alert(response.data.errorMsg)
        })
      }
    },
    mounted() {
      this.list_blog()
    }
  }
</script>
