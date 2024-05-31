import languages from 'misc/constants/languages';
const DEFAULT_LANG = languages.en;

function getMessages(lang) {
  const defaultMessages = require('./messages.json');
  let messages;
  try {
    messages = lang === DEFAULT_LANG
        ? defaultMessages
        : require(`./messages.${lang.toLowerCase()}.json`);
  } catch (e) {
    messages = defaultMessages;
  }
    return messages;}

export default getMessages;
