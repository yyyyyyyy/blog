<template>
  <div class="log">
    <div class="content-list">
      <ol>
        <li class="li-title">
          Note List
        </li>
        <li class="li-note" v-if="!add_flag" @click="add_on">
          Click Here 2 Add Note
        </li>
        <li class="li-note" v-if="add_flag">
          <input v-model="current_blog.name" @keyup.enter="edit_on" placeholder="BLOG NAME"/>
        </li>
        <li class="li-note" @click="get_blog_by_id(blog.id)" v-for="blog in blogs" :key="blog.id">
          {{blog.name}}
        </li>
      </ol>
    </div>
    <div class="content-content" v-if="edit_flag">
      <mavon-editor @save="operate" v-model="current_blog.content"/>
    </div>
  </div>
</template>
<script>
  import axios from 'axios'

  export default {
    name: 'write',
    data() {
      return {
        blogs: [],
        current_blog: {},
        new_blog: {},
        add_flag: false,
        edit_flag: false
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
      operate() {
        if (this.add_flag === true) {
          this.save()
        } else {
          this.update()
        }
      },
      save() {
        axios.post(this.global.serverPath + '/blog/insert', JSON.stringify(this.new_blog), {
          headers: {
            'Content-Type': 'application/json',
            withCredentials: true
          }

        }).then((response) => {
          if (response.data.success) {
            this.add_flag = false
            this.edit_flag = false
            this.new_blog = {}
            this.list_blog()
          } else {
            alert(response.data.errorMsg)
          }
        }).catch((response) => {
          alert(response)
        })
      },
      update() {
        axios.post(this.global.serverPath + '/blog/update', JSON.stringify(this.current_blog), {
          headers: {
            'Content-Type': 'application/json',
            withCredentials: true
          }
        }).then((response) => {
          if (response.data.success) {
            this.get_blog_by_id(this.current_blog.id)
            alert("successful!!!")
          } else {
            alert(response.data.errorMsg)
          }
        }).catch((response) => {
          alert(response.data.errorMsg)
        })
      },
      get_blog_by_id(id) {
        axios.get(this.global.serverPath + '/blog/getBlogById/' + id, {
          headers: {
            withCredentials: true
          }
        }).then((response) => {
          if (response.data.success) {
            if (response.data.result) {
              this.current_blog = response.data.result
              if (this.add_flag === true) {
                this.new_blog = this.current_blog
                this.add_flag = false
              }
              this.edit_flag = true
            }
          }
        }).catch((response) => {
          console.info(response)
          alert(response.data.errorMsg)
        })
      },
      add_on() {
        this.current_blog = this.new_blog
        this.add_flag = true
        if (!this.current_blog.name) {
          this.edit_flag = false
        }
      },
      edit_on() {
        if (!this.current_blog.name || typeof (this.current_blog.name) === 'undefined') {
          this.add_flag = false
          this.edit_flag = false
        } else {
          this.edit_flag = true
        }
      }
    },
    mounted() {
      this.list_blog()
    }
  }
</script>
