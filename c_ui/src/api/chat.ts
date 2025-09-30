import request from '@/utils/request.ts';

const url = '/chatApi/v1/chat/completions';
const headers = {
  'Content-Type': 'application/json',
  Accept: 'application/x-ndjson',
  Authorization: 'Bearer sk-tusvogeebpwumnkeofzzozovkwcndsfaaovakgjjvbzwyfnn'
};
export const sendMessage = async (messages: any) => {
  const body = {
    messages: messages,
    stream: true,
    model: 'deepseek-ai/DeepSeek-R1',
    max_tokens: 4096,
    presence_penalty: 0,
    frequency_penalty: 0,
    top_p: 0.7,
    temperature: 0.6
  };
  return await fetch(url, {
    headers: headers,
    method: 'POST',
    body: JSON.stringify(body)
  });
};
