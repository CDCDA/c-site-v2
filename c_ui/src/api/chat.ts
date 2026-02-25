import { ElNotification, ElMessage } from 'element-plus';

const baseUrl = '/chatApi/v1';
const apiKey = 'sk-XijV1GP29bY16k2TnkGje7eCkNLE14Ma63qpZ49OydIGtW6j';
const url = `${baseUrl}/chat/completions`;

const systemMessage = {
  role: 'system',
  content:
    '你是 Kimi，由 Moonshot AI 提供的人工智能助手，你更擅长中文和英文的对话。你会为用户提供安全，有帮助，准确的回答。同时，你会拒绝一切涉及恐怖主义，种族歧视，黄色暴力等问题的回答。Moonshot AI 为专有名词，不可翻译成其他语言。'
};

export const sendMessage = async (messages: any[]) => {
  const fullMessages = [systemMessage, ...messages];

  const body = {
    messages: fullMessages,
    stream: true,
    model: 'moonshot-v1-8k',
    max_tokens: 10240,
    presence_penalty: 0,
    frequency_penalty: 0,
    top_p: 0.7,
    temperature: 0.6
  };

  try {
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/x-ndjson',
        Authorization: `Bearer ${apiKey}`
      },
      method: 'POST',
      body: JSON.stringify(body)
    });

    if (!response.ok) {
      const errorText = await response.text();
      ElNotification.error(`API Error: ${response.status} - ${errorText}`);
      return response;
    }

    return response;
  } catch (error) {
    ElNotification.error(`请求失败: ${error}`);
    return error;
  }
};
