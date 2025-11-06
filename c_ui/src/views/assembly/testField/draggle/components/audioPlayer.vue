<template>
  <div class="audio-container">
    <!-- <div class="music-title">{{ musicName }}</div> -->
    <audio
      @timeupdate="updateProgress"
      controls
      ref="audioRef"
      style="display: none"
      preload="auto"
    >
      <source :src="props.song.musicUrl" type="audio/mpeg" />
      {{ $t('您的浏览器不支持音频播放') }}</audio>
    <div class="audio-right">
      <svg-icon iconName="commonSvg-前一个" class="dialogAudioPlay" @click="preSong"></svg-icon>
      <svg-icon
        :iconName="commonSvg - audioStatus == 'pause' ? $t('暂停') : $t('播放')"
        @click="playAudio"
        class="dialogAudioPlay"
      >
      </svg-icon>
      <svg-icon iconName="commonSvg-下一个" class="dialogAudioPlay" @click="nextSong"></svg-icon>
      <div class="progress-bar-bg" id="progressBarBg" v-dragto="setAudioIcon">
        <div class="progress-bar" id="progressBar"></div>
      </div>
      <div class="audio-time" style="min-height: 10px">
        <span class="audio-length-current" id="audioCurTime">{{ audioStart }}/</span>
        <span class="audio-length-total">{{ duration }}</span>
      </div>
      <div class="volume">
        <div
          @click.stop="
            () => {
              return false;
            }
          "
          class="volume-progress"
          v-show="audioHuds"
        >
          <div class="volume-bar-bg" id="volumeBarBg" v-adjuster="handleShowMuteIcon">
            <div class="volume-bar" id="volumeBar"></div>
          </div>
        </div>
        <svg-icon
          iconName="commonSvg-非静音"
          style="height: 30px; width: 30px"
          class="audio_high"
          @click.stop="audioHuds = !audioHuds"
        />
      </div>
      <svg-icon
        iconName="commonSvg-顺序播放"
        class="dialogAudioPlay"
        style="margin-right: 15px"
      ></svg-icon>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed, watch } from 'vue';
const props = defineProps({
  song: null
});

const emit = defineEmits(['getPreSong', 'getNextSong']);

const audioUrl = ref('');
watch(
  () => props.song.musicUrl,
  val => {
    audioUrl.value = 'http://music.163.com/song/media/outer/url?id=' + val + '.mp3';
    fetch();
  },
  {
    deep: true
    // immediate: true
  }
);

const musicName = $t('夜曲');
const audioStatus = ref('play');
const audioStart = ref('0:00');
const duration = ref('0:00');
const audioVolume = ref(0.5);
const audioHuds = ref(false);
const audioRef = ref();

function preSong() {
  emit('getPreSong');
}

function nextSong() {
  emit('getNextSong');
}

const VDragto = reactive({
  mounted: function (el, binding, vnode) {
    el.addEventListener(
      'click',
      e => {
        let wdiv = document.getElementById('progressBarBg').clientWidth;
        let audio = vnode.ctx.refs.audioRef;
        audio.load();
        // console.log(audio,77);
        // return;
        // 只有音乐开始播放后才可以调节，已经播放过但暂停了的也可以
        let ratemin = e.offsetX / wdiv;
        let rate = ratemin * 100;
        document.getElementById('progressBar').style.left = rate + '%';
        audio.currentTime = audio.duration * ratemin;

        audio.play();
        binding.value();
      },
      false
    );
  }
});
const VAdjuster = reactive({
  mounted: function (el, binding, vnode) {
    el.addEventListener(
      'click',
      e => {
        let hdiv = document.getElementById('volumeBarBg').clientHeight;
        let audio = vnode.ctx.refs.audioRef;
        // 只有音乐开始播放后才可以调节，已经播放过但暂停了的也可以
        let ratemin = e.offsetY / hdiv;
        let rate = ratemin * 100;
        document.getElementById('volumeBar').style.height = rate + '%';
        audio.volume = ratemin;
        binding.value(rate / 100);
      },
      false
    );
  }
});
onMounted(() => {
  fetch();
});

function fetch() {
  // let that = this;
  var myVid = audioRef.value;
  myVid.load();
  myVid.loop = false;
  // 监听音频播放完毕
  myVid.addEventListener(
    'ended',
    function () {
      audioStatus.value = 'play'; // 显示播放icon
      document.getElementById('progressBar').style.width = '0%'; // 进度条初始化
    },
    false
  );
  if (myVid != null) {
    myVid.oncanplay = function () {
      duration.value = transTime(myVid.duration); // 计算音频时长
    };
    myVid.volume = 0.5; // 设置音量50%
  }
}
// 播放暂停控制
function playAudio() {
  let recordAudio = audioRef.value; // 获取audio元素
  if (recordAudio.paused) {
    recordAudio.play();
    audioStatus.value = 'pause';
  } else {
    recordAudio.pause();
    audioStatus.value = 'play';
  }
}
// 更新进度条与当前播放时间
function updateProgress(e) {
  var value = e.target.currentTime / e.target.duration;
  if (document.getElementById('progressBar')) {
    document.getElementById('progressBar').style.left = value * 100 + '%';
    if (e.target.currentTime === e.target.duration) {
      audioStatus.value = 'pause';
    }
  } else {
    audioStatus.value = 'pause';
  }

  audioStart.value = transTime(audioRef.value.currentTime);
}
/**
 * 音频播放时间换算
 * @param {number} value - 音频当前播放时间，单位秒
 */
function transTime(time) {
  var duration = parseInt(time);
  var minute = parseInt(duration / 60);
  var sec = (duration % 60) + '';
  var isM0 = ':';
  if (minute === 0) {
    minute = '00';
  } else if (minute < 10) {
    minute = '0' + minute;
  }
  if (sec.length74 === 1) {
    sec = '0' + sec;
  }
  return minute + isM0 + sec;
}
function setAudioIcon() {
  audioStatus.value = 'pause';
}
function handleShowMuteIcon(val) {
  audioVolume.value = val;
}
computed(() => {
  function audioIcon() {
    if (audioHuds.value) {
      return audioVolume.value < 0.01 ? 'checked icon-jingyin' : 'checked icon-shengyin';
    } else {
      return 'icon-shengyin';
    }
  }
});
</script>

<style lang="scss" scoped>
.audio-container {
  width: calc(100% - 20px);
  padding: 0 10px;
  box-shadow: get('box-shadow');
  background: get('back');
}
@include theme() {
  .el-icon {
    color: get('font-color');
  }
  .music-title {
    position: absolute;
    left: 70px;
    top: 3px;
    font-size: 0.8rem;
    color: get('font-color');
  }
  .volume {
    position: relative;
    .volume-progress {
      position: absolute;
      top: -150px;
      width: 32px;
      height: 140px;
      background: get('audio-pro-bar');
      border-radius: 4px;
      padding-top: 10px;
    }
    .volume-bar-bg {
      margin: 0 auto;
      width: 6px;
      height: 120px;
      background: get('audio-pro-bar');
      border-radius: 100px;
      flex: 1;
      position: relative;
      transform: rotate(180deg);
      cursor: pointer;
      .volume-bar {
        width: 6px;
        height: 50%;
        background: get('audio-pro-bar');
        border-radius: 100px;
      }
    }
    .checked {
      color: get('audio-pro-bar');
    }
  }
  .audio-right {
    width: 100%;
    height: 100%;
    color: get('font-color');
    background: get('back');
    border-radius: 6px;
    display: flex;
    align-items: center;
    .dialogAudioPlay {
      width: 30px;
      height: 30px;
      cursor: pointer;
      margin-left: 15px;
    }
    .progress-bar-bg {
      background-color: get('font-color');
      flex: 1;
      position: relative;
      height: 8px;
      cursor: pointer;
      margin: 0 10px;
      border-radius: 5px;
    }
    .progress-bar {
      background-color: get('font-color');

      width: 20px;
      height: 20px;
      border-radius: 50%;
      bottom: 6px;
      position: relative;
    }

    .audio-time {
      overflow: hidden;
      font-size: 1rem;
      margin: 0 10px;
      .audio-length-total {
        float: right;
      }
      .audio-length-current {
        float: left;
      }
    }
  }
}

.audio_high {
  width: 50px;
  height: 50px;
}
</style>
