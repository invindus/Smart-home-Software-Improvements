#FULL CODE CAME FROM sample_graph_generation_code.py in the week 5 folder of the labs folder of the course

import os
import mysql.connector
import matplotlib.pyplot as plt

cnx = mysql.connector.connect(user="root", password="tmp")
db = cnx.cursor()

def execute_query():
    # gets daily electricity usage
    query = ("SELECT group_experiment, create_time, SUM(minutes_lights_on) "
             "FROM TartanHome.Home "
             "WHERE minutes_lights_on IS NOT NULL and group_experiment IS NOT NULL "
             "GROUP BY create_time, group_experiment "
             )

    db.execute(query)
    results = db.fetchall()
    print(results)
    return results

def preprocess_data(raw_data):
    prep = list(map(lambda t : (t[0], t[1], float(t[2])/(60*1000)), raw_data ))
    result = dict()
    print()
    for e in prep:
        if e[0] in result:
            result[e[0]][0].append(e[1])
            result[e[0]][1].append(e[2])
        else:
            result[e[0]] = [[ e[1] ],[ e[2] ]]
    print(result)
    return result

def generate_charts(good_data):
    plt.plot(good_data['1'][0], good_data['1'][1], label="Group 1", color='red', linewidth=1)
    plt.plot(good_data['2'][0], good_data['2'][1], label="Group 2", color='orange', linewidth=1)
    plt.legend()
    plt.show()

raw_data = execute_query()
good_data = preprocess_data(raw_data)
generate_charts(good_data)

db.close()
cnx.close()