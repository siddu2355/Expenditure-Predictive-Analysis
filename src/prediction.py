import numpy as np
from sklearn.ensemble import RandomForestRegressor
import sys

months = np.array(eval(sys.argv[1]))
expenses = np.array(eval(sys.argv[2]))

model = RandomForestRegressor(n_estimators=100, random_state=42)
model.fit(months.reshape(-1, 1), expenses)

next_month = np.array([[months[-1] + 1]])
predicted_expense = round(model.predict(next_month)[0])

print(predicted_expense)
