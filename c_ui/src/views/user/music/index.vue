<!--
 * @Description: 音乐
-->
<template>
  <div class="page-main music-main">
    <div class="music-song animated c-left">
      <div class="music-header">
        <span>歌单</span><svg-icon iconName="commonSvg-切换" @click="changeSearch"> </svg-icon>
      </div>
      <div class="music-search" v-if="isSearch">
        <el-input v-model="searchText"></el-input>
        <div class="song-list" style="height: calc(100% - 40px)">
          <div
            class="song"
            v-for="(song, i) in searchSongs"
            :class="song.id == selectSong.id ? 'is-selected' : ''"
            @click="songClick(song)"
          >
            <span class="song-name">
              <span class="song-index">{{ i + 1 }}</span
              >{{ song.name }}</span
            >
            <span class="song-singer">{{ song.author }}</span>
          </div>
        </div>
      </div>
      <div class="song-list" style="height: calc(100% - 40px)" v-else>
        <div
          class="song"
          v-for="(song, i) in songs"
          :class="song.id == selectSong.id ? 'is-selected' : ''"
          @click="songClick(song)"
        >
          <span class="song-name">
            <span class="song-index">{{ i + 1 }}</span
            >{{ song.name }}</span
          >
          <span class="song-singer">{{ song.author }}</span>
        </div>
      </div>
    </div>
    <div class="music-info animated c-right">
      <img :src="selectSong.album?.picUrl" />
      <h2>{{ selectSong.album?.name }}</h2>
      <span>{{ getAuthors(selectSong.album?.artists) }}</span>
      <p>{{ formatDate(new Date(selectSong.album?.publishTime), 'YY-MM-dd') }}</p>
      <div class="music-info-edit"></div>
    </div>
  </div>
  <musicPlayer
    class="music-player"
    :song="selectSong"
    @getNextSong="getNextSong"
    @getPreSong="getPreSong"
  ></musicPlayer>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { formatDate } from '@/utils/date.ts';
import musicPlayer from '@/views/assembly/testField/draggle/components/audioPlayer.vue';
import { listSong, getSongById } from '@/api/music.ts';
import { ElMessage } from 'element-plus';
const searchSongs = ref([] as any);
const router = useRouter();
const isSearch = ref(false as any);
const searchText = ref('' as any);
const selectSong = ref({
  id: 1,
  name: '师父',
  author: '王朝1982,卦者灵风,朱旭BooBoo',
  intro: '有意思的歌',
  userId: 1,
  releaseTime: '2023-03-30 16:00:00'
} as any);
const songs = ref([] as any);
const songList = ref([] as any);

function songClick(song: any) {
  getSongData(song.wyyId);
}

function getAuthors(artists: any) {
  if (!artists) return null;
  let authors = '';
  artists.forEach((a: any, i: any) => {
    authors = authors + a.name + (i != artists.length - 1 ? ',' : '');
  });
  return authors;
}

async function getSongData(id: any) {
  let params = { id: id, ids: [id] };
  const { code, mag, songs } = (await getSongById(params)) as any;
  if (code == 200) {
    selectSong.value = songs[0];
  }
}

function changeSearch() {
  isSearch.value = !isSearch.value;
  // if (isSearch.value) {
  //   songs.value = [];
  // } else {
  //   songs.value = JSON.parse(JSON.stringify(songList.value));
  // }
}

function getNextSong() {
  if (songs.value.length <= 0) return;
  songs.value.some((song: any, i: any) => {
    if (song.id == selectSong.value.id) {
      if (songs.value[i + 1]) selectSong.value = songs.value[i + 1];
      else ElMessage.info('已经到最后一首了');
      return true;
    }
  });
}

function getPreSong() {
  if (songs.value.length <= 0) return;
  songs.value.some((song: any, i: any) => {
    if (song.id == selectSong.value.id) {
      if (songs.value[i - 1]) selectSong.value = songs.value[i - 1];
      else ElMessage.info('已经到第一首了');
      return true;
    }
  });
}

async function getSongList() {
  const { code, data } = (await listSong({})) as any;
  if (code == 200) {
    songs.value = rows;
    getSongData(songs.value[0].wyyId);
  }
}
onMounted(() => {
  getSongList();
});
</script>
<style lang="scss" scoped>
@include theme() {
  .music-main {
    color: get('font-color');
    background: transparent !important;
    backdrop-filter: none !important;
  }
  .song,
  img,
  .music-song,
  .music-info {
    background: get('back');
    box-shadow: get('box-shadow');
  }
  .music-player {
    background: get('back');
  }
}

.music-main.page-main {
  display: flex;
  justify-content: space-between;
}
.music-song {
  width: calc(100% - 540px);
  border-radius: 8px;
  padding: 10px;
  height: calc(100vh - 100px);
  overflow: auto;
  overflow-x: hidden;
  .song {
    height: 30px;
    display: flex;
    cursor: pointer;
    justify-content: space-between;
    align-items: center;
    padding: 6px 15px;
    border-radius: 5px;
    margin-bottom: 3px;
    transition: all 0.2s linear;
  }
  .song:hover,
  .song.is-selected {
    // height: 40px;
    transform: scale(1.01);
    // font-size: 1rem;
    margin: 5px 0;
    .song-name {
      font-weight: bold;
    }
  }
  .song-index {
    font-style: italic;
    // font-size: 22px;
    margin-right: 15px;
  }
  .song-singer {
    opacity: 0.6;
  }
}
// <div class="music-info">
//       <img src="" />
//       <h2>{{ selectSong.name }}</h2>
//       <span>{{ selectSong.quality }}</span>
//     </div>
//     <div class="music-player"></div>
.music-info {
  width: 460px;
  border-radius: 8px;
  height: calc(100vh - 140px);
  // border: 1px solid;
  padding: 30px 20px;
  img {
    width: 250px;
    height: 230px;
    border-radius: 8px;
    object-fit: cover;
    background-position: center;
  }
  h2 {
  }
  span {
    font-size: 22px;
  }
  p {
    font-size: 1rem;
    opacity: 0.8;
  }
}
.music-player {
  position: absolute;
  width: 100vw;
  height: 80px;
  bottom: 0;
  left: 0;
  border-radius: 0 !important;
}
.music-header {
  padding: 5px 20px;
  width: calc(100% - 30px);
  text-align: left;
  font-size: 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  .svg-icon {
    width: 25px;
    height: 25px;
    cursor: pointer;
  }
}
</style>
