import argparse
import requests
import time
import random
import enum
import urllib3

urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

class ServicePackageType(enum.Enum):
    BASIC = "BASIC"
    STANDARD = "STANDARD"
    GOLD = "GOLD"

def click_ad(service_package):
    url = "https://localhost:8090/api/commercials/click"
    params = {'servicePackage': service_package.value}
    response = requests.get(url, params=params, verify=False)
    if response.status_code == 200:
        print(f"Successfully clicked ad with package: {service_package.value}")
    else:
        print(f"Failed to click ad with package: {service_package.value}, status code: {response.status_code}")

def simulate_ad_clicks(service_package):
    while True:
        click_ad(service_package)
        time.sleep(0.1)

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Simulate ad clicks with specified service package type.")
    parser.add_argument("type", type=str, choices=[e.value for e in ServicePackageType], help="Service package type: BASIC, STANDARD, GOLD")

    args = parser.parse_args()

    service_package = ServicePackageType[args.type]
    simulate_ad_clicks(service_package)
