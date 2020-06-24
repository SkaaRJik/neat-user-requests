function getAlphabet() {
  const alphabet = [];

  for (let i = "A".charCodeAt(0); i < "Z".charCodeAt(0); i++) {
    alphabet.push(String.fromCharCode(i));
    if (i % 10 === 0) alphabet.push(" ");
  }
  alphabet.push(" ");
  for (let i = "a".charCodeAt(0); i < "z".charCodeAt(0); i++) {
    alphabet.push(String.fromCharCode(i));
    if (i % 10 === 0) alphabet.push(" ");
  }
  return alphabet;
}


export const alphabet = getAlphabet();
