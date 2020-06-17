export default {
  Linear_method: (data, minRange, maxRange) =>
    new Promise((resolve, reject) => {
      if (!data) reject("Data is null");
      if (!data[0]) reject("Data is empty");
      let min = 0;
      let max = 0;
      const mins = [];
      const maxs = [];
      const output = [];
      const statistic = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];

      for (let i = 0; i < data[0].length; i++) {
        for (let j = 0; j < data.length; j++) {
          if (i == 0) {
            output.push([]);
          }
          if (j == 0) {
            if (data[j][i] != null) {
              min = data[j][i];
              max = data[j][i];
            }
            continue;
          }
          if (data[j][i] != null) {
            min = Math.min(min, data[j][i]);
            max = Math.max(max, data[j][i]);
          }
        }
        mins.push(min);
        maxs.push(max);
        for (let j = 0; j < data.length; j++) {
          if (data[j][i] == null) {
            output[j].push(null);
            continue;
          }
          const newValue = (data[j][i] - min) / (max - min);
          if (minRange >= 0) {
            const rangedValue = newValue * (maxRange - minRange) + minRange;
            output[j].push(rangedValue);
          } else {
            const rangedValue = newValue * (maxRange * 2) + minRange;
            output[j].push(rangedValue);
          }
          let statisticIndex = Math.floor(newValue * 10);
          if (statisticIndex > 10 || statisticIndex < 0) {
            statisticIndex = 11;
          }
          statistic[statisticIndex]++;
        }
      }
      resolve({
        minRange,
        maxRange,
        output,
        mins,
        maxs,
        statistic
      });
    })
};
