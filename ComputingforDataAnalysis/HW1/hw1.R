data = read.csv("hw1_data.csv")

# Q1
print(names(data))

# Q2
print(data[1:2,])

# Q3
print(dim(data))

# Q4
print(tail(data, 2))

# Q5
print(data[47,"Ozone"])

# Q6
print(sum(is.na(data[,"Ozone"])))

# Q7
print(mean(data[,"Ozone"], na.rm=TRUE))
print(mean(data[,"Ozone"][!is.na(data[,"Ozone"])]))

# Q8
subdata = subset(data, Ozone > 31 & Temp > 90)
print(mean(subdata[,"Solar.R"]))

# Q9
subdata = subset(data, Month == 6)
print(mean(subdata[,"Temp"]))

# Q10
subdata = subset(data, Month == 5)
print(max(subdata[,"Ozone"], na.rm=TRUE))